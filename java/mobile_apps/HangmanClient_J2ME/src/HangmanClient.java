/*
 * HangmanClient.java
 *
 * Created on December 6, 2006, 6:52 PM
 */

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.io.*;
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
/**
 * @author  Ali Javadzadeh - Aria Vedjdani
 * @version 2.0
 */
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
public class HangmanClient extends MIDlet implements CommandListener{
   
    final static String ERROR_MSG_SEND="Unable to send data to server";
    final static String ERROR_MSG_CONNECT="Unable to connect to hangman server";
    final static String GAME_MSG_WRONG_GUESS="Wrong guess!";
    final static String GAME_MSG_GAMEOVER="Game over, You lose! The correct word was";
    final static String GAME_MSG_CORRECT_GUESS="Correct guess!";
    final static String CONNECT_MSG="Connecting to server...Please wait";
    final static String GAME_MSG_START="Game started...Try to guess the word!";
    final static String GAME_MSG_REPEATED_GUESS="You have  guessed this letter before!";
    final static String GAME_MSG_CONGRATULATIONS="Congratulations! You win!";
    final static String CONNECT_FIRST_MSG="***Connect to Hangman Server First!***";
    final static String CLIENT_MSG_STARTGAME="start game";
    final static String CLIENT_MSG_ENDGAME="end game";
    final static String CLIENT_MSG_CONTINUE_GAME="continue game";
    final static String SERVER_MSG_CONGRATULATIONS="!Congratulations";
    final static String SERVE_MSG_GAMEOVER="$game over;you lose";
    final static String MAX_ATTEMPTS="5";
     //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    private Form connectForm;
    private Form gameForm;
    private Ticker ticker = new Ticker("");
    private Ticker connectticker = new Ticker(CONNECT_FIRST_MSG);
    private Command connectCommand;
    private Command newGameCommand;
    private Command guessCommand;
    private Command exitCommand;
    private Display display;
    private TextField serverIP;
    private TextField serverPort;
    private TextField guessText;
    private TextField word;
    private TextField wrongLetters;
    private TextField leftAttempts;
    private TextField totalScore;
    private String strMsg="";
    private String counter=MAX_ATTEMPTS;
    private String serverMsg="";
    private SocketConnection socketConnection;
    private OutputStream out;
    private InputStream in;
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public HangmanClient()
    {
        super();
        display = Display.getDisplay( this );
	connectForm = new Form("Hangman Server");
        connectForm.setTicker(connectticker);
        serverIP = new TextField("Server IP", "127.0.0.1", 50, 0);
        serverPort = new TextField("Port", "80", 50, 0);
        connectCommand = new Command("Connect to Server", Command.SCREEN, 1);
        newGameCommand = new Command("New Game", Command.SCREEN, 1);
        guessCommand = new Command("Guess!", Command.SCREEN, 1);
        exitCommand=new Command("Exit", Command.SCREEN, 2);
        newGameCommand = new Command("New Game", Command.SCREEN, 1);
        guessCommand = new Command("Guess!", Command.SCREEN, 1);
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public void startApp() {
        connectForm.addCommand(connectCommand);
	connectForm.addCommand(exitCommand);
	connectForm.append(serverIP);
	connectForm.append(serverPort);
        connectForm.setCommandListener( this );
        display.setCurrent( connectForm );
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public void initGameForm()
    {
        System.out.println("----------------initGameForm-----------");
        gameForm = new Form("Hangman Game");
        gameForm.addCommand(guessCommand);
        gameForm.addCommand(newGameCommand);
        gameForm.addCommand(exitCommand);
        gameForm.setCommandListener( this );
        word=new TextField("Word","",50,TextField.UNEDITABLE);
        wrongLetters=new TextField("Wrong Letters","",50,TextField.UNEDITABLE);
        guessText=new TextField("Your Guess", "", 50, 0);
        totalScore=new TextField("TotalScore", "0", 50,TextField.UNEDITABLE );
        leftAttempts=new TextField("Attempts Left", "0", 50,TextField.UNEDITABLE );
        gameForm.append(guessText);
        gameForm.append(word);
        gameForm.append(wrongLetters);
        gameForm.append(leftAttempts);
        gameForm.append(totalScore);
        gameForm.setTicker(ticker);
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
     public void commandAction(Command c, Displayable s) 
     {
         System.out.println("----------------Command Action-----------");
         if(c== connectCommand)
         {
             strMsg=CONNECT_MSG;
          try{ 
           connect(serverIP.getString(),serverPort.getString());
           initGameForm();
           display.setCurrent(gameForm);
           StartGame();
           Play("");
         }
          catch(IOException e)
          {
              strMsg=ERROR_MSG_CONNECT;
          }
             connectForm.getTicker().setString(strMsg);
         }
         ////////////////////////////////
         else if( c == guessCommand ) 
         {	
           try{
	   String guess = guessText.getString().trim();
          // System.out.println("guess: "+guess);
           if(guess.length()==1 && !(this.wrongLetters.getString().indexOf(guess)<0 && this.word.getString().indexOf(guess)<0)){
                 serverMsg=GAME_MSG_REPEATED_GUESS;
                 this.ticker.setString(serverMsg);
           }
           else //if(guess.length()>=1)
           {
           byte[] data = guess.getBytes();
	   out.write(data, 0, data.length);				
	   out.flush();	
            Play(this.guessText.getString());
           }
          }
          catch(IOException e)
          {
              gameForm.getTicker().setString(ERROR_MSG_SEND);
              e.printStackTrace();
          }
           catch(Exception e)
           {
               e.printStackTrace();
           }
	 }
         ////////////////////////////////
         else if(c==newGameCommand)
         {
             resetAll();
             continueGame();
             Play("");
         }
         ////////////////////////////////
         else if( c == exitCommand ) {
             try{
             System.out.println("Exit");
              quitGame();
           
             }
             catch(IOException e)
             {
                 this.ticker.setString(serverMsg);
                 e.printStackTrace();
             }
              destroyApp(false);			
	      notifyDestroyed();
         }
         
     }
     //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
      public void connect(String server, String port)throws IOException
    {
          System.out.println("----------------Connect-----------");
           socketConnection=(SocketConnection)Connector.open("socket://" + server + ":"+port);
           out=socketConnection.openOutputStream();
           in=socketConnection.openInputStream();
    }
       //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        public void StartGame()
        {
           System.out.println("----------------Start Game-----------");
           try {
            byte[] startup_text = CLIENT_MSG_STARTGAME.getBytes();
            out.write(startup_text, 0, startup_text.length);
            out.flush();
        }
        catch (IOException e) {
            
            System.err.println(e);
            this.ticker.setString(ERROR_MSG_SEND);
	}
        }
        //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
     public void Play(String sent_letter )
      {
         System.out.println("----------------Play-----------");
          byte[] received_text = new byte[200];
           int n=0;
         try {
           n=in.read(received_text,0,200);
           String  strReply=new String(received_text).substring(0,n);
           //System.out.println(strReply);
           int attemptIndex=strReply.indexOf('#');
           int scoreIndex=strReply.lastIndexOf('#');
           if(scoreIndex<0)scoreIndex=strReply.length();
           //System.out.println("Score Index: "+String.valueOf(scoreIndex));
           String answer=strReply.substring(0,attemptIndex);
           String attemptsLeft=strReply.substring(attemptIndex+1,strReply.length());
           //System.out.println(answer);
           //System.out.println("attemptsLeft:"+attemptsLeft);
           //System.out.println("counter:"+counter);
           if(answer.equals(SERVER_MSG_CONGRATULATIONS))
           {
                System.out.println("--Congratulations--");
                this.word.setString(strReply.substring(attemptIndex+1,scoreIndex)); 
                 this.totalScore.setString(strReply.substring(scoreIndex+1,strReply.length()));
                 this.serverMsg=GAME_MSG_CONGRATULATIONS;
           }
           else if(counter.compareTo(attemptsLeft)>0)
           {
                 this.wrongLetters.setString(this.wrongLetters.getString()+" "+sent_letter); // word.setString(answer);
                 System.out.println("------wrong guess------");
                 this.word.setString(answer);
                 counter=attemptsLeft;
                  this.serverMsg=GAME_MSG_WRONG_GUESS;
                   this.leftAttempts.setString(attemptsLeft);
           }
           else if(counter.compareTo(attemptsLeft)==0)
           {
               if(!sent_letter.equals(""))//Correct guess
               {
                   this.serverMsg=GAME_MSG_CORRECT_GUESS;
                    this.leftAttempts.setString(counter);
               }
               else serverMsg=GAME_MSG_START;
                this.word.setString(answer);
         
           }
           else if(answer.equals(SERVE_MSG_GAMEOVER))
           {
                this.word.setString(strReply.substring(strReply.indexOf('#')+1,strReply.lastIndexOf('#')));
                this.totalScore.setString(strReply.substring(scoreIndex+1,strReply.length()));
                this.leftAttempts.setString("0");
                this.serverMsg=GAME_MSG_GAMEOVER+ " ["+ this.word.getString()+"]";
           }
          gameForm.getTicker().setString(serverMsg);
        }
        catch (IOException e2) {
            System.out.println(e2);
            System.err.println(e2);
        }
     } 
     //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
     
     public void continueGame() 
     {
          System.out.println("------Continue Game------");
            try {
                out.write(CLIENT_MSG_CONTINUE_GAME.getBytes(), 0, 13);
                out.flush();
            } 
            catch (IOException e5) {
                serverMsg=ERROR_MSG_SEND;
                this.ticker.setString(serverMsg);
                System.err.println(e5);
                return;
            }
    }
     //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
     public void resetAll()
     {
         wrongLetters.setString("");
         leftAttempts.setString(MAX_ATTEMPTS);
         counter="5";
         guessText.setString("");
     }
      //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
     public void quitGame()throws IOException
     {
        System.out.println("------Quit Game------");
        byte[] end_text = CLIENT_MSG_ENDGAME.getBytes();
	try {
	out.write(end_text, 0, end_text.length);
	out.flush();
        out.close();
	in.close();
	socketConnection .close();
        
        } catch (IOException e) {
             
            System.err.println(e);
	}
     }
     //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
}
