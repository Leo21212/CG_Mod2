package Projeto;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import sun.font.TrueTypeFont;

/**
 *
 * @author Glauco
 */
public class Ovelha implements GLEventListener, KeyListener {

    GLU glu = new GLU();
    GLUT glut = new GLUT();
    float pos[] = {0,0,-10,1};
    
    public static void main(String args[])
    {
        new Ovelha();
    }
    private double g;
    private double incG;
    private double g2;
    
    public Ovelha()
    {
        GLJPanel canvas = new GLJPanel();
        canvas.addGLEventListener(this);
        
        JFrame frame = new JFrame("Exemplo01");
        frame.setSize(500, 500);
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
        frame.addKeyListener(this);
      
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        System.exit(0);
                    }
                }).start();
            }
        });

    
    }
    
    boolean espacoOn = false;
    boolean espacoOff = false;
    boolean[] keyStates = new boolean[256];
    
    @Override
    public void init(GLAutoDrawable drawable) {
        Animator a = new Animator(drawable);
        GL2 gl = drawable.getGL().getGL2();
        a.start();
        
        glut = new GLUT();

        
        gl.glClearColor(0.5f, 0.5f, 1f, 0.4f);
        
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);      
    }
    
    double movCerca = 20;
    double puloSobe = 0;
    double puloDesce = 10;
    double r = 0;
    float posZ = -10;
    float incZ = 0.1f;
    boolean fechou = false;
    
    @Override
    public void display(GLAutoDrawable drawable) {
        if(fechou==false){
        
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT |
                   GL.GL_DEPTH_BUFFER_BIT
        );
        
        gl.glLoadIdentity();
        gl.glTranslated(0,0,-20);
        
        gl.glLightfv(GL2.GL_LIGHT0,
                     GL2.GL_POSITION,
                     pos,
                     0);
        
        //Esse Rotated controla o giro da ovelha 1/2
        //gl.glRotated(g2, 0, 1, 0);          
        g2 = g2 + 0.1;
        
                                          
        if(g <= -90){
            incG = 0.2;
        }
        if(g >= -25){
            incG = -0.2;
        }
        g = g + incG;

        //Aqui esta controlando a luz
        pos[2]=20;
        posZ += incZ;
        
        if(posZ > 10)
            incZ = -0.1f;
        if(posZ < -10)
            incZ = 0.1f;
                           
        //Esse Rotated controla o giro da ovelha 2/2
        //gl.glRotated(r, 0, 1, 0);
        r = r + 0.05;
   
        //Tentando fazer o pular aqui
        if(espacoOn){
            if(puloSobe < 15){
                gl.glPushMatrix();
                    puloSobe = puloSobe+0.1; 
                    gl.glTranslated(0,puloSobe,0);
                    desenhaOvelha(gl);
                gl.glPopMatrix();
            }
            else if(puloDesce > 0){
                gl.glPushMatrix();
                    puloDesce = puloDesce-0.1; 
                    gl.glTranslated(0,puloDesce,0);
                    desenhaOvelha(gl);
                gl.glPopMatrix();
            }
            else{
                puloSobe = 0;
                puloDesce = 15;
                espacoOn = false;
            }  
        }  
        else
            desenhaOvelha(gl);
   
        desenhaSol(gl);
        desenhaChão(gl);
        
        if(movCerca > -30){
            gl.glPushMatrix();
                movCerca = movCerca-0.05;
                gl.glTranslated(movCerca,-0.8,0);
                desenhaCerca(gl);
            gl.glPopMatrix();
        }
        else{
            movCerca = 25;
        }
        
        if(movCerca>=-3.5 && movCerca<=3.5 && puloSobe<3)
        {
           fechou=true;
           gl.glClear(GL.GL_COLOR_BUFFER_BIT |
                   GL.GL_DEPTH_BUFFER_BIT
        );
           
        }
     }
    }
        
    public void reshape(GLAutoDrawable gLAutoDrawable, int x, int y, int w, int h) {
  
        GL2 gl = gLAutoDrawable.getGL().getGL2(); 
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(100,1,1,300);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslated(0,0,-10);
    }


    public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
      
    }
    
    @Override
    public void dispose(GLAutoDrawable glad) {
        
    }

    private void desenhaOvelha(GL2 gl) {
     gl.glPushMatrix();
        //Corpo
        gl.glPushMatrix();
           gl.glColor3d(1, 1, 1);
           gl.glScaled(2,1,1);
           glut.glutSolidCube(3);
        gl.glPopMatrix();
        
        //Cabeçinha
        gl.glPushMatrix();
           gl.glColor3d(0,0,0);
           gl.glScaled(0.5,0.5,0.5);
           gl.glTranslated(7.5,0.75,0);
           glut.glutSolidSphere(2, 10, 10);
        gl.glPopMatrix();
        
        //perna 1
        gl.glPushMatrix();
        gl.glPushMatrix();
            gl.glColor3d(0,0,0);
            gl.glTranslated(-2,-0.8,0.9);
            gl.glRotated(g,0,0,2);
            gl.glTranslated(1,-0.5,0);

            gl.glPushMatrix();
                gl.glScaled(2.5,1,1);
                glut.glutSolidCube(1);
            gl.glPopMatrix();
        gl.glPopMatrix();

        //perna 2
        gl.glPushMatrix();
            gl.glColor3d(0,0,0);
            gl.glTranslated(-2,-0.8,-0.9); 
            gl.glRotated(g,0,0,2);
            gl.glTranslated(1,-0.5,0); 

            gl.glPushMatrix();
                gl.glScaled(2.5,1,1);
                glut.glutSolidCube(1);
            gl.glPopMatrix();
        gl.glPopMatrix();

        //perna 3
        gl.glPushMatrix();
            gl.glColor3d(0,0,0);
            gl.glTranslated(2.5,-1,-0.9); 
            gl.glRotated(g,0,0,2);
            gl.glTranslated(1,-0.5,0); 

            gl.glPushMatrix();
                gl.glScaled(2,1,1);
                glut.glutSolidCube(1);
            gl.glPopMatrix();
        gl.glPopMatrix();

        //perna 4
        gl.glPushMatrix();
            gl.glColor3d(0,0,0);
            gl.glTranslated(2.5,-1,0.9); 
            gl.glRotated(g,0,0,2);
            gl.glTranslated(1,-0.5,0); 

            gl.glPushMatrix();
                gl.glScaled(2,1,1);
                glut.glutSolidCube(1);
            gl.glPopMatrix();
        gl.glPopMatrix();
      gl.glPopMatrix();
    }
    
    private void desenhaChão(GL2 gl){
        gl.glPushMatrix();
           gl.glColor3d(0.5, 1, 0.5);
           gl.glScaled(90,1,10);
           gl.glTranslated(-1,-5,0);
           glut.glutSolidCube(3);     
        gl.glPopMatrix();
    }
    
    private void desenhaCerca(GL2 gl){
        gl.glPushMatrix();
           gl.glColor3d(0.5f, 0.35f, 0.05f);
           gl.glScaled(0.5,1.5,2);
           //gl.glTranslated(20,-0.7,0);
           glut.glutSolidCube(3);
        gl.glPopMatrix();
    }
    
    private void desenhaSol(GL2 gl){
        gl.glPushMatrix();
           gl.glColor3d(1.0, 1.0, 0.0);
           //gl.glScaled(0.5,1.5,2);
           gl.glTranslated(15,15,0);
           glut.glutSolidSphere(3,20,20);
        gl.glPopMatrix();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char pressed = e.getKeyChar();
        if(pressed == ' ')
            espacoOn = true;
        if(pressed == 'r' && fechou == true){
            fechou = false;
            puloSobe = 0;
            puloDesce = 15;
            espacoOn = false;
            movCerca=20;
            
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
      
    }
}