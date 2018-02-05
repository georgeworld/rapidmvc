/*
 * Programming by: George <GeorgeNiceWorld@gmail.com>
 * Copyright (C) George(www.georgeinfo.com), All Rights Reserved.
 */
package mainentry;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import rapidmvc.annotation.RapidMvcCommentItem;
import rapidmvc.annotation.RapidMvcComments;

/**
 *
 * @author George <GeorgeNiceWorld@gmail.com>
 * @contact http://www.georgeinfo.com
 * @since 2015-07-07
 */
@RapidMvcComments({
    @RapidMvcCommentItem(dateTime = "2015-07-07 16:29", version = "v1.0.0", notes = {"创建本项目"})
    ,
    @RapidMvcCommentItem(dateTime = "2015-10-31 15:50", version = "v1.1.0", notes = {"将{WebAction}抽象成接口，创建了{DefaultWebActionImpl}实现类，用来作为默认的Http请求分发器；",
        "修改了{DefaultWebActionImpl}类中的bug，使得在一次Http会话结束后，将{AcHelper}从ThreadLocal中移除。"})
    ,
    @RapidMvcCommentItem(dateTime = "2017-07-27 09:59", version = "v1.1.1", notes = {"将未完成的遍历多叉树算法，改成不报错（仅仅是改成了不报错而已，并不能真的使用），以便于编译整个类库。"})
})
public class RapidMvcMain {

    public static final String VERSION = "v1.1.1";
    public static final String UPDATE_DATE_TIME = "2017-07-27 09:59";

    public RapidMvcMain() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setTitle("Rapid MVC Framework");
        mainFrame.setSize(599, 300);
        mainFrame.setLayout(new java.awt.BorderLayout());

        JLabel topLabel = new JLabel("快速MVC框架 " + getVersion());
        topLabel.setFont(new java.awt.Font("宋体", 1, 18));
        topLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(0, 102, 102), new java.awt.Color(0, 204, 204)));

        JLabel bottomLabel = new JLabel("(C)www.georgeinfo.com");
        bottomLabel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                openUrl("http://www.georgeinfo.com");
            }

            public void mousePressed(MouseEvent e) {
//                openUrl("http://www.georgeinfo.com");
            }

            public void mouseReleased(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseEntered(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseExited(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        bottomLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottomLabel.setForeground(Color.BLUE);

//        JTextArea changeLogArea = new JTextArea();
//        changeLogArea.setText(IrapidCommentsTool.getCommentsString(IrapidMain.class));
//        changeLogArea.setFont(new java.awt.Font("宋体", 0, 18));
//        JScrollPane mainScrollPane = new JScrollPane();
//        mainScrollPane.setViewportView(changeLogArea);
        mainFrame.add(topLabel, java.awt.BorderLayout.NORTH);
        mainFrame.add(new RapidMvcChangeLogPanel(RapidMvcMain.class), java.awt.BorderLayout.CENTER);
        mainFrame.add(bottomLabel, java.awt.BorderLayout.SOUTH);

        mainFrame.setAlwaysOnTop(true);
        setWindowCenter(mainFrame);
        mainFrame.setVisible(true);
    }

    public static String getVersion() {
        return "[" + VERSION + "]  build [" + UPDATE_DATE_TIME + "]";
    }

    /*
     * 使窗口居中显示
     */
    public static void setWindowCenter(Window window) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        int w = window.getWidth();
        int h = window.getHeight();
        window.setLocation((width - w) / 2, (height - h) / 2);
    }

    public static void openUrl(String urlStr) {
        try {
            //帮助
            URI url = new URI(urlStr);
            java.awt.Desktop.getDesktop().browse(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }
}
