//Basic Mouse Application Template
//
// sets up a window and puts some text on the screen.
// not threaded.

// Uses MouseListener for buttons
// Uses MouseMotionListener for mouse movement and dragging

// To use the mouse
// Add 
//    implements MouseListener
//    canvas.addMouseListener(this);
//    the required methods
//
//       public void mouseClicked(MouseEvent e)
//       public void mousePressed(MouseEvent e)
//       public void mouseReleased(MouseEvent e)



//K. Chun 8/2018

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

//Keyboard and Mouse
import java.awt.event.*;


//*******************************************************************************
// Class Definition Section

public class BasicKeyboardApp implements MouseListener, MouseMotionListener, KeyListener, Runnable {

    //Variable Definition Section
    //Declare the variables used in the program
    //You can set their initial values too

    //Sets the width and height of the program window
    final int WIDTH = 600;
    final int HEIGHT = 400;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public BufferStrategy bufferStrategy;

    //Mouse position variables
    public int mouseX, mouseY;

    //button
    public Button button1;

    public Image backgroundpic;

    public Image endgamepic;


    //timer variables

    public Image explosionPic;
    public Image DuckPic;
    public long startTime;
    public long currentTime;
    public long elapsedTime;

    public int amountClicked;


    public Duck theDuck;

    public Duck duck;

    public boolean startTimer;

    public Duck[] ducks;


    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        BasicKeyboardApp myApp = new BasicKeyboardApp();   //creates a new instance of the app
        new Thread(myApp).start();  //start up the thread
    }


    // Constructor Method
    // This has the same name as the class
    // This section is the setup portion of the program
    // Initialize your variables and construct your program objects here.
    public BasicKeyboardApp() {

        setUpGraphics();
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addKeyListener(this);
        backgroundpic = Toolkit.getDefaultToolkit().getImage("duck_background.png");
        DuckPic = Toolkit.getDefaultToolkit().getImage("Duck.png");
        explosionPic = Toolkit.getDefaultToolkit().getImage("explosion.png");
        theDuck = new Duck(400, 300, 3, -4, DuckPic);
        button1 = new Button(300, 300, 150, 60, "Reset Timer");
        ducks = new Duck[10];
        for(int i=0; i<ducks.length; i++) {
            ducks[i] = new Duck((int) (Math.random()*100), (int)(Math.random()*100), (int)(Math.random()*10), -4, DuckPic);
        }

    }// BasicGameApp()
    public void moveThings() {
        theDuck.move();
        for(int i=0; i< ducks.length;i++){
            ducks[i].move();
        }
    }


    //*******************************************************************************
//User Method Section
//
// put your code to do things here.
    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        JPanel panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.


        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE");

    }

    //paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
       // g.setColor(Color.BLACK);
        //g.setFont(new Font("TimesRoman", Font.BOLD, 25));
        //g.drawString("Mouse and Time Example", 100, 50);
       // g.drawString("Mouse Cursor is at  " + mouseX + ",  " + mouseY, 100, 150);

       g.drawImage(backgroundpic,0,0,WIDTH,HEIGHT,null);
        for(int i=0; i< ducks.length;i++) {
            if (ducks[i].isAlive == false) {
                //theDuck.hasExploded = true;
                ducks[i].dx = 0;
                ducks[i].dy = 0;
                ducks[i].currentTime = System.currentTimeMillis();
                ducks[i].elapsedTime = ducks[i].currentTime - ducks[i].startTime;


                if (ducks[i].elapsedTime < 200) {
                    g.drawImage(explosionPic, ducks[i].xpos, ducks[i].ypos, ducks[i].width, ducks[i].height, null);
                } else{
                   ducks[i].dx = 20000;
                }
                

            } else {
                g.drawImage(ducks[i].pic, ducks[i].xpos, ducks[i].ypos, ducks[i].width, ducks[i].height, null);
            }
        }

//       for(int i=0; i< ducks.length;i++){
//           g.drawImage(ducks[i].pic, ducks[i].xpos, ducks[i].ypos, ducks[i].width, ducks[i].height, null);
//       }


        //button
//        g.setColor(Color.YELLOW);
//        g.fillRect(button1.xpos, button1.ypos, button1.width, button1.height);
//        g.setColor(Color.BLACK);
//        g.drawString(button1.text, button1.xpos + 5, button1.ypos + 40);

        //time
        g.drawString(amountClicked+"",10,50);
        
        g.dispose();
        bufferStrategy.show();
    }


    // main thread
    // this is the code that plays the game after you set things up
    public void run() {


        while (true) {

            //get the current time
            currentTime = System.currentTimeMillis();

            //calculate the elapsed time
            elapsedTime = currentTime-startTime;
moveThings();
            render();  // paint the graphics
            pause(10); // sleep for 10 ms
        }
    }

    public void pause(int time) {
        //sleep
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }
public void explosion(){

        while (theDuck.counterOn=true){
            System.out.println("this worked");
            theDuck.counter=theDuck.counter+1;
        }
        if (theDuck.counter > 1){
            System.out.println("this worked");
        }

    }

    //*******************************************************************************

    // REQUIRED KEYBOARD METHODS
    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        //System.out.println("Key Pressed: " + key);

        if (key == 'd') {
            //System.out.println("Yeah");
        }
    }

    public void keyReleased(KeyEvent e) {
        char key = e.getKeyChar();
        //System.out.println("Key Released: " + key);

    }

    public void keyTyped(KeyEvent e) {
        // The getKeyModifiers method is a handy //way to get a String
        // representing the //modifier key.
        //System.out.println("Key Typed: " + e.getKeyChar() + " "
               // + KeyEvent.getKeyModifiersText(e.getModifiers()) + "\n");
    }
    //*******************************************************************************

    // REQUIRED Mouse Listener methods
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        int x, y;
        x = e.getX();
        y = e.getY();

        mouseX = x;
        mouseY = y;
        //System.out.println();
        //System.out.println("Mouse Clicked at " + x + ", " + y);

        for (int i = 0; i < ducks.length; i++) {
            ducks[i].counterOn = true;
            //explosion();
            System.out.println("this worked");
            ducks[i].counter = ducks[i].counter + 1;

            if (ducks[i].rec.contains(x, y)) {
                System.out.println("hello");
                amountClicked += 1;
                ducks[i].isAlive = false;
                ducks[i].startTime = System.currentTimeMillis();

            }

        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {


        //System.out.println();
        //System.out.println("Mouse Button Released");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //System.out.println();
        //System.out.println("Mouse has entered the window");

    }

    @Override
    public void mouseExited(MouseEvent e) {
        //System.out.println();
        //System.out.println("Mouse has left the window");

    }

    // REQUIRED Mouse Listener methods
    @Override
    public void mouseDragged(MouseEvent e) {
        //System.out.println("Mouse is being dragged");
    }

    public void mouseMoved(MouseEvent e) {

        int x, y;
        x = e.getX();
        y = e.getY();
        mouseX = x;
        mouseY = y;
        //System.out.println("Mouse moving");
        //System.out.println("Mouse is at  " + x + ", " + y);

    }


}