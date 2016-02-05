
import acm.graphics.*;
import acm.program.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram{
	public static final int POINTS_PER_BRICK = 100;
	public static final int  APPLICATION_WIDTH= 1900;//(BRICK_WIDTH +BRICK_SEP ) * NBRICKS_PER_ROW;;
	public static final int APPLICATION_HEIGHT= 2000;//getHeight();
	private static final int WIDTH= APPLICATION_WIDTH;
	private static final int HEIGHT= APPLICATION_HEIGHT;
	private static final int PADDLE_WIDTH= 600;
	private static final int PADDLE_HEIGHT=10;
	private static final int PADDLE_Y_OFFSET = 80;
	private static final int NBRICKS_PER_ROW=1;
	private static final int NBRICK_ROWS =10;
	private static final int BRICK_SEP= 5;
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW-1)* BRICK_SEP)/NBRICKS_PER_ROW;
	private static final int BRICK_HEIGHT= 20;
	private static final int BALL_DIAM= 20;
	private static final int BRICK_Y_OFFSET = 50;
	private static final int NTURNS =4;
	
	public static void main(String[] args){
		new Breakout().start(args);
	}
	
	public void run(){
		//titleScreen();
		setUp();
		initSounds();
		waitForSpace();
		startUp();
		while (gameOver== false){
			 moveBall();
			 checkForCollisions();
		     lifeLost();
		}
		youLoose();
	}
	
	private void initSounds(){
		sounds = new AudioSounds();
		sounds.addSound("click.wav");
		sounds.addSound("applause-1.wav");
		sounds.addSound("bounce.wav");
		sounds.addSound("Aww.wav");
	}
	
	private void titleScreen(){
		setSize(APPLICATION_WIDTH,APPLICATION_HEIGHT);
		GImage title= new GImage("Title Screen.png",APPLICATION_WIDTH/4,100);
		title.scale(2);
		add(title);
		playButton = new GRoundRect(APPLICATION_WIDTH/2-43,500,100,40,5,5);
		playButton.setFilled(true);
		playButton.setFillColor(Color.RED);
		add(playButton);
		playText = new GLabel("PLAY",APPLICATION_WIDTH/2-30,530);
		playText.setFont(new Font("LabelFont",20, 30));
		add(playText);
		
		while(playButtonBool){
			pause(10);
		}
		remove(title);
		remove(playButton);
		remove(playText);
		
	}
	
	private void setUp(){
		addMouseListeners();
		addKeyListeners();
		Color[] brickColors = {Color.RED,Color.RED,Color.ORANGE,Color.ORANGE,Color.YELLOW,Color.YELLOW,Color.GREEN,Color.GREEN,Color.CYAN,Color.CYAN}; 
		Lives = new GLabel("Lives:" + NTURNS,25,38);
		Lives.setFont("Arial-35");
		add(Lives);
		Points = new GLabel ("Score: 0",APPLICATION_WIDTH-400,38);
		Points.setFont("Arial-35");
		add(Points);
		GImage logo = new GImage("MakeLogo.jpg",APPLICATION_WIDTH/2 - 60,6);
		logo.scale(0.05);
		add(logo);
		int rectX= 10;
		int rectY= BRICK_Y_OFFSET-(BRICK_HEIGHT + BRICK_SEP);
		for(int i=0; i<NBRICK_ROWS; i++){
			rectY= rectY + BRICK_HEIGHT + BRICK_SEP;
			rectX=10;
			for(int j=0; j<NBRICKS_PER_ROW; j++){
				GRoundRect rect= new GRoundRect (rectX,rectY, BRICK_WIDTH,BRICK_HEIGHT,1,1);
				rect.setFilled(true);
				rect.setFillColor(brickColors[i%10]);
				add(rect);
				rectX= rectX + BRICK_WIDTH + BRICK_SEP;
			}
			
		}
		paddle = new GRoundRect (WIDTH/2- BRICK_WIDTH /2,HEIGHT- PADDLE_Y_OFFSET - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT,5,5);
		paddle.setFilled(true);
		add(paddle);
		ball = new GOval (ballX,ballY,BALL_DIAM,BALL_DIAM);
		ball.setFilled(true);
		ball.setColor(Color.WHITE);
		ball.setFillColor(Color.RED);
		add(ball);
		
	}

	
	public void startUp(){
		GLabel label = new GLabel("READY...");
		label.setFont("Arial Bold-30");
		label.setColor(Color.RED);
		add(label,getWidth()/2-label.getWidth()/2,getHeight()/2);
		for(int i=255; i>0; i--){
			label.setColor(new Color(label.getColor().getRed(),label.getColor().getGreen(),label.getColor().getBlue(),i));
			pause(3);
		}
		label.setColor(Color.ORANGE);
		label.setLabel("SET...");
		label.setLocation(getWidth()/2-label.getWidth()/2,getHeight()/2);
		for(int i=255; i>0; i--){
			label.setColor(new Color(label.getColor().getRed(),label.getColor().getGreen(),label.getColor().getBlue(),i));
			pause(3);
		}
		label.setColor(Color.GREEN);
		label.setLabel("GO!!!");
		label.setFont("Arial Bold-80");
		label.setLocation(getWidth()/2-label.getWidth()/2,getHeight()/2);
		for(int i=255; i>0; i--){
			label.setColor(new Color(0,255,0,i));
			pause(3);
		}
		remove(label);
		
	}
	
	public void mouseClicked(MouseEvent e){
		GObject object = getElementAt(e.getX(),e.getY());
		print("Hi");
		if (object == playButton || object == playText){
			print("GOOD");
			playButtonBool = false  ;
		}
	}
	
	public void mouseMoved(MouseEvent e){
		paddle.setLocation(e.getX(),getHeight()- PADDLE_Y_OFFSET);
	}
	private void moveBall(){
		ball.move(moveX, moveY);
		pause(2);
		ballX=ballX+moveX;
		ballY=ballY+moveY;
	}
	
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			spacePressed = true;
		}
	}
	
	private void waitForSpace(){
		while(true){
			if(spacePressed){
				spacePressed = false;
				return;
			}
			pause(1);
		}
	}
	
	private void checkForCollisions() {
		if (ballY + BALL_DIAM >= HEIGHT || ballY <= BRICK_Y_OFFSET-(BRICK_HEIGHT + BRICK_SEP)+20) {
			moveY = -moveY;
		}
		if (ballX + BALL_DIAM >= WIDTH || ballX <= 0) {
			moveX = -moveX;
		}
		if (ballY+ BALL_DIAM == paddle.getY() & ballX < paddle.getX() + PADDLE_WIDTH + BALL_DIAM & ballX > paddle.getX() -BALL_DIAM) {
			/*GLabel speedChange = new GLabel("SpeedChange!",200,200);
			speedChange.setFont("Arial-30");
			speedChange.setColor(Color.RED);
			add(speedChange);*/
			//int newX=1;
			/*if(moveX<0){
				moveX=moveX;
			}else{
				moveX=moveX;
			}*/
			//int newY= 1;
			moveY=moveY;
			moveY = -moveY;
			//pause(100);
			//remove(speedChange);
		}
		if(ballY + BALL_DIAM!= paddle.getY()){
			gobj = getElementAt(ballX,ballY);
			/*if (gobj == Lives || gobj == Points || gobj == rect2){
			    gobj=null;
			}*/
			if(gobj instanceof GRect && gobj != paddle){
				moveY = -moveY;
				GRect rect = ((GRect)gobj);
				if(rect.getFillColor() == Color.CYAN ){
					points+= 1000;
				}
				if(rect.getFillColor() == Color.GREEN ){
					points+= 2000;				
								}
				if(rect.getFillColor() == Color.YELLOW){
					points+= 3000;	
				}
				if(rect.getFillColor() == Color.ORANGE){
					points+= 4000;	
				}
				if(rect.getFillColor() == Color.RED){
					points+= 5000;	
				}
				remove(gobj);
				String X = "Points:  " + points;
				Points.setLabel(X);
				bricksHit++;
				
			}
		}
	}
	
	private void lifeLost(){
		if(ballY + BALL_DIAM >= HEIGHT){
			livesLeft = livesLeft-1;
			ballX= 10;
			ballY= 350;
			ball.setLocation(ballX, ballY);
			moveX=1;
			moveY=1;
			String X = "Lives:"+ livesLeft;
			Lives.setLabel(X);
			points-= 500;
			String Y= "Points: "+ points;
			Points.setLabel(Y);
			if(livesLeft==0){
				gameOver=true;
				return;
			}
			waitForSpace();
			      
			startUp();
		}
		if (bricksHit == NBRICK_ROWS * NBRICKS_PER_ROW){
			youWin();
		}
	}
	private void youWin(){
		gameOver = true;
		pause(1000);
		removeAll();
		sounds.play("applause-1.wav");
		
		GImage happy = new GImage("Smiley.jpg");
		happy.scale(2);
		add(happy);
		happy.setLocation(getWidth()/2 - happy.getWidth()/2,80);
		
		GLabel  label = new GLabel("YOU WON!!");
		label.setFont("Helvetica-75");
		label.setColor(Color.RED);
		add(label);
		label.setLocation((getWidth()/2) - (label.getBounds().getWidth()/2),60);
		
		String Y = "You had " + livesLeft + " lives left and " + points + " points when you won the game!";
		GLabel  label2 = new GLabel(Y,0,0);
		label2.setFont("Helvetica-15");
		add(label2);
		label2.setLocation(getWidth()/2 - label2.getBounds().getWidth()/2,80);
		
		GLabel prize = new GLabel("Congrats! Take a prize from the box.");
		prize.setFont("Arial-40");
		add(prize);
		prize.setLocation((getWidth()/2)-(prize.getBounds().getWidth()/2),800);
		
		Color[] winColors = {Color.RED,Color.ORANGE,Color.YELLOW,Color.GREEN,Color.CYAN};
		for(int i = 0; i<33; i++){
			label.setColor(winColors[i%5]);
			for(int j =0; j<20;j++){
				label.move(0,2);
				label2.move(0, 1);
				pause(10);
				label.setColor(new Color(label.getColor().getRed(),label.getColor().getGreen(),label.getColor().getBlue(),label.getColor().getAlpha()-10));
			}
		}
		reset();
	}
	private void youLoose(){
		removeAll();
		sounds.play("Aww.wav");
		GLabel label2 = new GLabel("You lost, but you still get a prize from the box!");
		label2.setFont("Arial-40");
		add(label2);
		label2.setLocation(getWidth()/2 - label2.getBounds().getWidth()/2, 100);
		
		GImage happy = new GImage("Frowny.jpg");
		happy.scale(2);
		add(happy);
		happy.setLocation(getWidth()/2-happy.getWidth()/2,150);
		
		GLabel  label = new GLabel("GAME OVER");
		label.setFont("Helvetica-75");
		label.setColor(Color.RED);
		label.setLocation((getWidth()/2) - (label.getBounds().getWidth()/2),60);
		add(label);
		for(int j =0; j<400;j++){
			label.move(0,2);
			pause(10);
		}
		reset();
	}
	
	private void reset(){
		waitForSpace();
		removeAll();
		ballX = 10;
		ballY = 350;
		gameOver = false;
		livesLeft = NTURNS;
		bricksHit = 0;
		points = 0;
		run();
	}
	private void calibrate(){
		removeAll();
		GLabel message = new GLabel("Calibrating Gyroscope",500,400);
		message.setFont("Arial-70");
		add(message);
		GLabel message2 = new GLabel("Please Wait...",550,600);
		message2.setFont("Arial-30");
		add(message2);
		waitForClick();
	}

private boolean go = false;
private GObject gobj;
private boolean gameOver;	
private boolean spacePressed = false;
private int livesLeft=NTURNS;
private GOval ball;	
private GRoundRect paddle;
private int ballX=10;
private int ballY=350;
private int moveX=1;
private int moveY=1;
private int points;
private int bricksHit;
private GLabel Points;
private GLabel Lives;
private GRect rect2;
private Boolean playButtonBool= true;
private GRoundRect playButton;
private GLabel playText;
private AudioSounds sounds;
Color[] brickColors = {Color.RED,Color.RED,Color.ORANGE,Color.ORANGE,Color.YELLOW,Color.YELLOW,Color.GREEN,Color.GREEN,Color.CYAN,Color.CYAN}; 
}
