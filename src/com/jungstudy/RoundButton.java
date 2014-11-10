package com.jungstudy;

	import java.awt.*;

	import java.awt.geom.*;

	import javax.swing.*;

	import java.awt.event.*;



	public class RoundButton extends JButton {

	    public RoundButton(String label) {

	        super(label);

	        

	        //下面的语句讲述这个按钮变为一个圆形而不是椭圆形

	        Dimension size = getPreferredSize();

	        size.width = size.height = Math.max(size.width, size.height);

	        setPreferredSize(size);

	        //不让JButton画背景而允许我们去画一个圆背景

	        setContentAreaFilled(false);



	    }





	    // 画出圆的背景和标签

	    protected void paintComponent(Graphics g) {

	        if (getModel().isArmed()) {

	            g.setColor(Color.lightGray);

	        } else {

	            g.setColor(getBackground());

	        }

	        g.fillOval(0, 0, getSize().width-1, getSize().height-1);

	        // 在焦点上画出一个标签

	        super.paintComponent(g);



	    }





	    // 画出一个边框

	    protected void paintBorder(Graphics g) {

	        g.setColor(getForeground());

	        g.drawOval(0, 0, getSize().width-1, getSize().height-1);

	    }







	    // 侦察单击区域

	    Shape shape;



	    public boolean contains(int x, int y) {

	        // 如果按钮改变了尺寸将重新创建一个Shape

	        if (shape == null || !shape.getBounds().equals(getBounds())) {

	            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());

	        }

	        return shape.contains(x, y);

	    }



public static void callfun(){
	System.out.println("you click button1!");
}



	    // 测试

	    public static void main(String[] args) {

	        JButton button1 = new RoundButton("http://www.cn-java.com");

	        JButton button2 = new RoundButton("欢迎你常来做客");

	        button1.setBackground(Color.green);

	        button2.setBackground(Color.yellow);

	        // 创建一个Fram来显示这个按钮

	        JFrame frame = new JFrame();

	        frame.getContentPane().add(button1);

	        frame.getContentPane().add(button2);

	        frame.getContentPane().setLayout(new FlowLayout());

	        frame.setSize(450, 350);

	        frame.setVisible(true);

	        //测试单击事件

	        button1.addActionListener(new ActionListener(){

	                        public void actionPerformed(ActionEvent e){

	                                callfun();

	                        }

	                });

	        button2.addActionListener(new ActionListener(){

	                        public void actionPerformed(ActionEvent e){

	                                System.out.println("you click button2!");

	                        }

	                });



	    }

	}