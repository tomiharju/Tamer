package com.me.tamer.gameobjects.renders;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerStage;

/**
 * @author Kesyttäjät
 * This class is the superclass for all objects that needs to be animated
 * This class has all the attributes needed for animation
 *
 */

public class AnimatedRenderer implements Renderer {

	private Sprite sprite ;
	private ArrayList<Animation> animations;
	private int currentAnimation = 0;
	
	private Texture spriteSheet;
	private TextureRegion[][] frames;
	private TextureRegion[] effectFrames;
	private TextureRegion currentFrame;
	private float stateTime;
	private float animationDuration = 3;
	private Vector2 size = new Vector2();
	private Vector2 pos = new Vector2();
	private float angle;
	
	//Shader test
	private ShaderProgram shader;
	private ShaderProgram defaultShader;
	private float vtime = 0;
	private Texture tex0,tex1;
	int u_worldView,a_position;
	
	private TamerStage stage;
	
	final String VERT =  
			"attribute vec4 "+ShaderProgram.POSITION_ATTRIBUTE+";\n" +
			//"attribute vec4 "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
			//"attribute vec2 "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
			
			"uniform mat4 u_projTrans;\n" + 
			" \n" + 
			"varying vec4 vColor;\n" +
			"varying vec2 vTexCoord;\n" +
			
			"void main() {\n" +  
			//"	vColor = "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
			//"	vTexCoord = "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
			"	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
			"}";
	
	public AnimatedRenderer(){
		animations = new ArrayList<Animation>();
		
		stage = TamerStage.instance();
		
		//SHADER STUFF
		FileHandle handle = Gdx.files.classpath("com/me/tamer/utils/VertexShader");
		String vertexShader = handle.readString();
		
		handle = Gdx.files.classpath("com/me/tamer/utils/FragmentShader");
		String fragmentShader = handle.readString();
		
		//shader = new ShaderProgram(VERT, fragmentShader);
		shader = new ShaderProgram(vertexShader, fragmentShader);
		
		if(shader.isCompiled())System.out.println("shader compiled");
		System.out.println(shader.getLog());
		
		
		tex0 = new Texture(Gdx.files.internal("data/graphics/button_scream.png"));
		/*tex1 = new Texture(Gdx.files.internal("data/graphics/button_spear.png"));
		mask = new Texture(Gdx.files.internal("data/graphics/joystick.png"));
		*/
		
		//important since we aren't using some uniforms and attributes that SpriteBatch expects
		ShaderProgram.pedantic = false;
		
		
		shader.begin();
		shader.setUniformi("u_texture1", 1);
		shader.setUniformf("iResolution",100f,100f,0);
		shader.setUniformf("iMouse", 100f,100f,0,0);
		
		shader.end();
		
		//bind dirt to glActiveTexture(GL_TEXTURE1)
		tex0.bind(1);
		
		//now we need to reset glActiveTexture to zero!!!! since sprite batch does not do this for us
		Gdx.gl.glActiveTexture(GL10.GL_TEXTURE0);
		
		//vbatch = new SpriteBatch(1000, shader);
		//vbatch.setShader(shader);
		
		//vcam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//vcam.setToOrtho(false);
		
		//get locations
		u_worldView = shader.getUniformLocation("u_worldView");
		a_position = shader.getAttributeLocation("a_position");
		
		//default shader
		defaultShader = SpriteBatch.createDefaultShader();
		
	}

	@Override
	public void draw(SpriteBatch batch) {	
		
		if (!animations.isEmpty()){
			currentFrame = animations.get(currentAnimation).getKeyFrame(stateTime,true);
			
			//batch.setShader(shader);
			
			//shader.setUniformMatrix(u_worldView, stage.getCamera().combined);
			//shader.setAttributef("a_position", 100.0f, 1.0f, 50.0f, 1.0f);
			//shader.setUniformf("iGlobalTime", vtime+=Gdx.graphics.getDeltaTime());
			
			//currentFrame.getTexture().bind(1);
			
			batch.draw(currentFrame,pos.x - size.x / 2,pos.y, size.x, size.y);

			//batch.draw(tex0,pos.x - size.x / 2,pos.y - size.y /2, size.x, size.y);
			//batch.draw(tex0,100,100);
			
			//batch.flush();
			
			//batch.setShader(defaultShader);
			//batch.draw( currentFrame, pos.x - size.x / 2, pos.y - size.y /2, 0, 0, size.x, size.y, 1, 1, angle);
			
		}
	}

	@Override
	public void loadGraphics(String graphicsName) {
		
		sprite 	= new Sprite(new Texture(Gdx.files.internal("data/graphics/"+graphicsName+".png")));
		if(sprite == null)
			throw new IllegalArgumentException("Could not load sprite!");
		
		
		animations.add(new Animation(animationDuration,sprite));
	}
	
	@Override
	public void loadGraphics( String animName,int FRAME_COLS,int FRAME_ROWS ){
		
		
		//loadGraphics from spritesheet
		spriteSheet = new Texture(Gdx.files.internal("data/graphics/animations/"+animName+".png"));
		frames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 
				FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);          
		
		for (int i = 0; i < FRAME_ROWS; i++) {
			animations.add(new Animation(animationDuration,frames[i]));
		}

		stateTime = 0f;
	}
	
	
	public void setAnimSpeed(float speed){
		animationDuration = speed;
	}
	
	@Override
	public void setSize(float w, float h) {
		this.size.set(w,h);
	}
	@Override
	public void setSize(Vector2 size) {
		this.size.set(size);
		
	}
	@Override
	public void setPosition(Vector2 pos) {
		this.pos.set(pos.x, pos.y);	
	}

	

	@Override
	public void setOrientation(int orientation) {
		this.currentAnimation = orientation;
	}
	
	public void setAngle(float a){
		this.angle = a;
	}

	@Override
	public void setBounds(float x, float y, float width, float height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadEffect(String animName, int FRAME_COLS, int FRAME_ROWS,
			boolean looping,float speed) {
		// TODO Auto-generated method stub
		
	}

	


	
}
