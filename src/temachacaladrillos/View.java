package temachacaladrillos;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class View implements ModelListener{
    private final String TITLE_OF_THE_GAME = "TeMachacaLadrillos";
    private final Dimension btnDim = new Dimension(120, 30);
    private final Model model;
    private final Controller controller;
    private final JFrame ventana;
    private final Canvas canvas;
    JPanel btnPanel;
    private final JButton btnNew;
    private final JButton btnPause;
    private final JButton btnQuit;
    private final Font largeFont = new Font("Arial", Font.BOLD, 80);
    private final Font smallFont = new Font("Arial", Font.BOLD, 30);
    private final Color electricBlue = new Color(0, 158, 255);
    public View(){
        model = new Model();
        controller = new Controller(model);
        canvas = new Canvas();
        canvas.setBackground(Color.BLACK);
        canvas.setFocusable(true);
        canvas.setVisible(true);
        
        ventana = new JFrame();
        ventana.setSize(Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
        ventana.setLocationRelativeTo(null);
        ventana.setTitle(TITLE_OF_THE_GAME);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setFocusable(true);
        ventana.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_LEFT :
                        controller.movePaddleLeft();
                        break;
                    case KeyEvent.VK_RIGHT :
                        controller.movePaddleRight();
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void keyReleased(KeyEvent e){
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_N:
                        controller.startNewGame();
                        break;
                    case KeyEvent.VK_SPACE:
                        controller.pauseGame();
                        break;
                    default:
                        break;
                }
            }
        });
        ventana.add(canvas);
        
        btnNew = new JButton("New");
        btnNew.setPreferredSize(btnDim);
        btnNew.setToolTipText("Start new game");
        btnNew.setFocusable(false);
        btnNew.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startNewGame();
            }
        });
        
        btnPause = new JButton("Start/Pause");
        btnPause.setPreferredSize(btnDim);
        btnPause.setToolTipText("Pause game");
        btnPause.setFocusable(false);
        btnPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.pauseGame();
            }
        });

        btnQuit = new JButton("Quit");
        btnQuit.setPreferredSize(btnDim);
        btnQuit.setToolTipText("Quit the game");
        btnQuit.setFocusable(false);
        btnQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        btnPanel = new JPanel();
        btnPanel.setFocusTraversalKeysEnabled(false);
        btnPanel.add(btnNew);
        btnPanel.add(btnPause);
        btnPanel.add(btnQuit);
        
        ventana.add(btnPanel);        
        ventana.getContentPane().add(BorderLayout.CENTER, canvas);
        ventana.getContentPane().add(BorderLayout.SOUTH, btnPanel);
    }
    
    @Override
    public void updateView() {
        canvas.repaint();
    }

    public class Canvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw((Graphics2D) g);
        }
    }
    
    public void draw(Graphics2D g){
        //All drawing staff
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        //Field bounds
        
        g.setColor(Color.ORANGE);
        g.fillRect(0, Constants.FIELD_TOP_BOUND, Constants.FIELD_LEFT_BOUND, Constants.FIELD_BOTTOM_BOUND); //Left
        g.fillRect(Constants.FIELD_RIGHT_BOUND, Constants.FIELD_TOP_BOUND, Constants.WINDOW_WIDTH - Constants.FIELD_LEFT_BOUND, Constants.FIELD_BOTTOM_BOUND); //Right
        g.fillRect(0, Constants.FIELD_TOP_BOUND, Constants.WINDOW_WIDTH, 5); //Top
        
        //Bricks
        
        for(int i = 0; i < model.numBricks(); ++i){
            if (model.isBrickAlive(i)) {
                g.setColor(model.getBrickColor(i));
                g.fillRect(model.getBrickX(i), model.getBrickY(i), Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT);
                g.setStroke(new BasicStroke(3));
                g.setColor(Color.BLACK);
                g.drawRect(model.getBrickX(i), model.getBrickY(i), Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT);
            }
        }
        //paddle
        
        g.setColor(Color.GREEN.darker());
        g.fillRect(model.paddleX(), model.paddleY(), Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT);
        
        //ball
        
        g.setColor(Color.WHITE);
        g.fillOval(model.ballX(), model.ballY(), Constants.BALL_DIAMETER, Constants.BALL_DIAMETER);
        
        //score
        
        g.setColor(electricBlue);
        g.setFont(smallFont);
        g.drawString("TOTAL SCORE: " + model.getScore(), Constants.WINDOW_WIDTH / 2 - 140, 35);
        
        //info
        
        g.setColor(Color.MAGENTA);
        g.setFont(largeFont);
        g.drawString(model.getInfoString(), 90, 350);
        
    }
    
    public void start(){
        model.addListener(this);
        ventana.setVisible(true);
    }
}
