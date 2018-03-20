/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.georgeinfo.rapidmvc.dependents.uri;

import com.georgeinfo.rapidmvc.dependents.uri.internal.UriTemplateParser;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * A URI template.
 *
 * @author Paul Sandoz
 * @author Martin Matula
 * @author Gerard Davison (gerard.davison at oracle.com)
 */
public class UriTemplate {
    private static final String[] EMPTY_VALUES = new String[0];

    /**
     * Order the templates according to JAX-RS specification.
     * <p>
     * Sort the set of matching resource classes using the number of
     * characters in the regular expression not resulting from template
     * variables as the primary key, the number of matching groups
     * as a secondary key, and the number of explicit regular expression
     * declarations as the tertiary key.
     * </p>
     */
    public static final Comparator<UriTemplate> COMPARATOR = new Comparator<UriTemplate>() {

        @Override
        public int compare(UriTemplate o1, UriTemplate o2) {
            if (o1 == null && o2 == null) {
                return 0;
            }
            if (o1 == null) {
                return 1;
            }
            if (o2 == null) {
                return -1;
            }

            if (o1 == EMPTY && o2 == EMPTY) {
                return 0;
            }
            if (o1 == EMPTY) {
                return 1;
            }
            if (o2 == EMPTY) {
                return -1;
            }

            // Compare the number of explicit characters
            // Note that it is important that o2 is compared against o1
            // so that a regular expression with say 10 explicit characters
            // is less than a regular expression with say 5 explicit characters.
            int i = o2.getNumberOfExplicitCharacters() - o1.getNumberOfExplicitCharacters();
            if (i != 0) {
                return i;
            }

            // If the number of explicit characters is equal
            // compare the number of template variables
            // Note that it is important that o2 is compared against o1
            // so that a regular expression with say 10 template variables
            // is less than a regular expression with say 5 template variables.
            i = o2.getNumberOfTemplateVariables() - o1.getNumberOfTemplateVariables();
            if (i != 0) {
                return i;
            }

            // If the number of template variables is equal
            // compare the number of explicit regexes
            i = o2.getNumberOfExplicitRegexes() - o1.getNumberOfExplicitRegexes();
            if (i != 0) {
                return i;
            }

            // If the number of explicit characters and template variables
            // are equal then comapre the regexes
            // The order does not matter as long as templates with different
            // explicit characters are distinguishable
            return o2.pattern.getRegex().compareTo(o1.pattern.getRegex());
        }
    };

    /**
     * A strategy interface for processing parameters, should be replaced with
     * a JDK 8 one day in the future.
     */
    private static interface TemplateValueStrategy {
        /**
         * Get a value for a given template variable.
         *
         * @param templateVariable template variable.
         * @param matchedGroup     matched group string for a given template variable.
         * @return template value.
         *
         * @throws java.lang.IllegalArgumentException in case no value has been found and the strategy
         *                                            does not support {@code null} values.
         */
        public String valueFor(String templateVariable, String matchedGroup);
    }

    /**
     * The regular expression for matching URI templates and names.
     */
    private static final Pattern TEMPLATE_NAMES_PATTERN = Pattern.compile("\\{([\\w\\?;][-\\w\\.,]*)\\}");

    /**
     * The empty URI template that matches the {@code null} or empty URI path.
     */
    public static final UriTemplate EMPTY = new UriTemplate();
    /**
     * The URI template.
     */
    private final String template;
    /**
     * The normalized URI template. Any explicit regex are removed to leave
     * the template variables.
     */
    private final String normalizedTemplate;
    /**
     * The pattern generated from the template.
     */
    private final PatternWithGroups pattern;
    /**
     * True if the URI template ends in a '/' character.
     */
    private final boolean endsWithSlash;
    /**
     * The template variables in the URI template.
     */
    private final List<String> templateVariables;
    /**
     * The number of explicit regular expressions declared for template
     * variables.
     */
    private final int numOfExplicitRegexes;

    /**
     * The number of regular expression groups in this pattern.
     */
    private final int numOfRegexGroups;

    /**
     * The number of characters in the regular expression not resulting
     * from conversion of template variables.
     */
    private final int numOfCharacters;

    /**
     * Constructor for {@code NULL} template.
     */
    private UriTemplate() {
        this.template = this.normalizedTemplate = "";
        this.pattern = PatternWithGroups.EMPTY;
        this.endsWithSlash = false;
        this.templateVariables = Collections.emptyList();
        this.numOfExplicitRegexes = this.numOfCharacters = this.numOfRegexGroups = 0;
    }

    /**
     * Construct a new URI template.
     * <p>
     * The template will be parsed to extract template variables.
     * </p>
     * <p>
     * A specific regular expression will be generated from the template
     * to match URIs according to the template and map template variables to
     * template values.
     * </p>
     *
     * @param template the template.
     * @throws PatternSyntaxException   if the specified
     *                                  regular expression could not be generated
     * @throws IllegalArgumentException if the template is {@code null} or
     *                                  an empty string.
     */
    @SuppressWarnings("DuplicateThrows")
    public UriTemplate(String template) throws PatternSyntaxException, IllegalArgumentException {
        this(new UriTemplateParser(template));
    }

    /**
     * Construct a new URI template.
     * <p>
     * The template will be parsed to extract template variables.
     * <p>
     * A specific regular expression will be generated from the template
     * to match URIs according to the template and map template variables to
     * template values.
     * <p>
     *
     * @param templateParser the parser to parse the template.
     * @throws PatternSyntaxException   if the specified
     *                                  regular expression could not be generated
     * @throws IllegalArgumentException if the template is {@code null} or
     *                                  an empty string.
     */
    @SuppressWarnings("DuplicateThrows")
    protected UriTemplate(UriTemplateParser templateParser) throws PatternSyntaxException, IllegalArgumentException {
        this.template = templateParser.getTemplate();

        this.normalizedTemplate = templateParser.getNormalizedTemplate();

        this.pattern = initUriPattern(templateParser);

        this.numOfExplicitRegexes = templateParser.getNumberOfExplicitRegexes();

        this.numOfRegexGroups = templateParser.getNumberOfRegexGroups();

        this.numOfCharacters = templateParser.getNumberOfLiteralCharacters();

        this.endsWithSlash = template.charAt(template.length() - 1) == '/';

        this.templateVariables = Collections.unmodifiableList(templateParser.getNames());
    }

    /**
     * Create the URI pattern from a URI template parser.
     *
     * @param templateParser the URI template parser.
     * @return the URI pattern.
     */
    private static PatternWithGroups initUriPattern(UriTemplateParser templateParser) {
        return new PatternWithGroups(templateParser.getPattern(), templateParser.getGroupIndexes());
    }
    /**
     * Get the URI template as a String.
     *
     * @return the URI template.
     */
    public final String getTemplate() {
        return template;
    }


    /**
     * Get the URI pattern.
     *
     * @return the URI pattern.
     */
    public final PatternWithGroups getPattern() {
        return pattern;
    }

    /**
     * Check if the URI template ends in a slash ({@code '/'}).
     *
     * @return {@code true} if the template ends in a '/', otherwise false.
     */
    @SuppressWarnings("UnusedDeclaration")
    public final boolean endsWithSlash() {
        return endsWithSlash;
    }

    /**
     * Get the list of template variables for the template.
     *
     * @return the list of template variables.
     */
    public final List<String> getTemplateVariables() {
        return templateVariables;
    }

    /**
     * Ascertain if a template variable is a member of this
     * template.
     *
     * @param name name The template variable.
     * @return {@code true} if the template variable is a member of the template, otherwise {@code false}.
     */
    @SuppressWarnings("UnusedDeclaration")
    public final boolean isTemplateVariablePresent(String name) {
        for (String s : templateVariables) {
            if (s.equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the number of explicit regular expressions declared in the template variables.
     *
     * @return the number of explicit regular expressions in the template variables.
     */
    public final int getNumberOfExplicitRegexes() {
        return numOfExplicitRegexes;
    }

    /**
     * Get the number of regular expression groups
     *
     * @return the number of regular expressions groups
     */
    public final int getNumberOfRegexGroups() {
        return numOfRegexGroups;
    }

    /**
     * Get the number of characters in the regular expression not resulting
     * from conversion of template variables.
     *
     * @return the number of explicit characters
     */
    public final int getNumberOfExplicitCharacters() {
        return numOfCharacters;
    }

    /**
     * Get the number of template variables.
     *
     * @return the number of template variables.
     */
    public final int getNumberOfTemplateVariables() {
        return templateVariables.size();
    }

    /**
     * Match a URI against the template.
     * <p>
     * If the URI matches against the pattern then the template variable to value
     * map will be filled with template variables as keys and template values as
     * values.
     * <p>
     *
     * @param uri                     the uri to match against the template.
     * @param templateVariableToValue the map where to put template variables (as keys)
     *                                and template values (as values). The map is cleared before any
     *                                entries are put.
     * @return true if the URI matches the template, otherwise false.
     *
     * @throws IllegalArgumentException if the uri or
     *                                  templateVariableToValue is null.
     */
    public final boolean match(CharSequence uri, Map<String, String> templateVariableToValue) throws
            IllegalArgumentException {
        if (templateVariableToValue == null) {
            throw new IllegalArgumentException();
        }

        return pattern.match(uri, templateVariables, templateVariableToValue);
    }

    /**
     * Match a URI against the template.
     * <p>
     * If the URI matches against the pattern the capturing group values (if any)
     * will be added to a list passed in as parameter.
     * <p>
     *
     * @param uri         the uri to match against the template.
     * @param groupValues the list to store the values of a pattern's
     *                    capturing groups is matching is successful. The values are stored
     *                    in the same order as the pattern's capturing groups.
     * @return true if the URI matches the template, otherwise false.
     *
     * @throws IllegalArgumentException if the uri or
     *                                  templateVariableToValue is null.
     */
    public final boolean match(CharSequence uri, List<String> groupValues) throws
            IllegalArgumentException {
        if (groupValues == null) {
            throw new IllegalArgumentException();
        }

        return pattern.match(uri, groupValues);
    }

    /**
     * Create a URI by substituting any template variables
     * for corresponding template values.
     * <p>
     * A URI template variable without a value will be substituted by the
     * empty string.
     *
     * @param values the map of template variables to template values.
     * @return the URI.
     */
    public final String createURI(final Map<String, String> values) {
        final StringBuilder sb = new StringBuilder();
        resolveTemplate(normalizedTemplate, sb, new TemplateValueStrategy() {
            @Override
            public String valueFor(String templateVariable, String matchedGroup) {
                return values.get(templateVariable);
            }
        });
        return sb.toString();
    }

    /**
     * Create a URI by substituting any template variables
     * for corresponding template values.
     * <p>
     * A URI template variable without a value will be substituted by the
     * empty string.
     *
     * @param values the array of template values. The values will be
     *               substituted in order of occurrence of unique template variables.
     * @return the URI.
     */
    public final String createURI(String... values) {
        return createURI(values, 0, values.length);
    }

    /**
     * Create a URI by substituting any template variables
     * for corresponding template values.
     * <p>
     * A URI template variable without a value will be substituted by the
     * empty string.
     *
     * @param values the array of template values. The values will be
     *               substituted in order of occurrence of unique template variables.
     * @param offset the offset into the template value array.
     * @param length the length of the template value array.
     * @return the URI.
     */
    public final String createURI(final String[] values, final int offset, final int length) {

        TemplateValueStrategy ns = new TemplateValueStrategy() {
            private final int lengthPlusOffset = length + offset;
            private int v = offset;
            private final Map<String, String> mapValues = new HashMap<String, String>();

            @Override
            public String valueFor(String templateVariable, String matchedGroup) {
                // Check if a template variable has already occurred
                // If so use the value to ensure that two or more declarations of
                // a template variable have the same value
                String tValue = mapValues.get(templateVariable);
                if (tValue == null) {
                    if (v < lengthPlusOffset) {
                        tValue = values[v++];
                        if (tValue != null) {
                            mapValues.put(templateVariable, tValue);
                        }
                    }
                }

                return tValue;
            }
        };

        final StringBuilder sb = new StringBuilder();
        resolveTemplate(normalizedTemplate, sb, ns);
        return sb.toString();
    }

    /**
     * Build a URI based on the parameters provided by the variable name strategy.
     *
     * @param normalizedTemplate normalized URI template. A normalized template is a template without any explicit regular
     *                           expressions.
     * @param builder            URI string builder to be used.
     * @param valueStrategy      The template value producer strategy to use.
     */
    private static void resolveTemplate(
            String normalizedTemplate,
            StringBuilder builder,
            TemplateValueStrategy valueStrategy) {
        // Find all template variables
        Matcher m = TEMPLATE_NAMES_PATTERN.matcher(normalizedTemplate);

        int i = 0;
        while (m.find()) {
            builder.append(normalizedTemplate, i, m.start());
            String variableName = m.group(1);
            // TODO matrix
            char firstChar = variableName.charAt(0);
            if (firstChar == '?' || firstChar == ';') {
                final char prefix;
                final char separator;
                final String emptyValueAssignment;
                if (firstChar == '?') {
                    // query
                    prefix = '?';
                    separator = '&';
                    emptyValueAssignment = "=";
                } else {
                    // matrix
                    prefix = ';';
                    separator = ';';
                    emptyValueAssignment = "";
                }

                int index = builder.length();
                String[] variables = variableName.substring(1).split(", ?");
                for (String variable : variables) {
                    try {
                        String value = valueStrategy.valueFor(variable, m.group());
                        if (value != null) {
                            if (index != builder.length()) {
                                builder.append(separator);
                            }

                            builder.append(variable);
                            if (value.isEmpty()) {
                                builder.append(emptyValueAssignment);
                            } else {
                                builder.append('=');
                                builder.append(value);
                            }
                        }
                    } catch (IllegalArgumentException ex) {
                        // no value found => ignore the variable
                    }
                }

                if (index != builder.length() && (index == 0 || builder.charAt(index - 1) != prefix)) {
                    builder.insert(index, prefix);
                }
            } else {
                String value = valueStrategy.valueFor(variableName, m.group());

                if (value != null) {
                    builder.append(value);
                }
            }

            i = m.end();
        }
        builder.append(normalizedTemplate, i, normalizedTemplate.length());
    }

    @Override
    public final String toString() {
        return pattern.toString();
    }

    /**
     * Hash code is calculated from String of the regular expression
     * generated from the template.
     *
     * @return the hash code.
     */
    @Override
    public final int hashCode() {
        return pattern.hashCode();
    }

    /**
     * Equality is calculated from the String of the regular expression
     * generated from the templates.
     *
     * @param o the reference object with which to compare.
     * @return true if equals, otherwise false.
     */
    @Override
    public final boolean equals(Object o) {
        if (o instanceof UriTemplate) {
            UriTemplate that = (UriTemplate) o;
            return this.pattern.equals(that.pattern);
        } else {
            return false;
        }
    }

    private static boolean notEmpty(String string) {
        return string != null && !string.isEmpty();
    }


}
