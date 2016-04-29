package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;


public class InputProcessor extends InputAdapter {

	public static int bufX;
	public static int bufY;
	public static  int point = 0;
	public static  int point2 = 0;

	@Override public boolean keyDown(int k){

		if(k == Keys.UP){
			GameKeys.setKey(GameKeys.UP, true);
		}
		else if(k == Keys.DOWN){
			GameKeys.setKey(GameKeys.DOWN, true);
		}
		else if(k == Keys.RIGHT){
			GameKeys.setKey(GameKeys.RIGHT, true);
		}
		else if(k == Keys.LEFT){
			GameKeys.setKey(GameKeys.LEFT, true);
		}
		else if(k == Keys.SPACE){
			GameKeys.setKey(GameKeys.SPACE, true);
		}
		return true;
	}

	@Override public boolean keyUp(int k){
		if(k == Keys.UP){
			GameKeys.setKey(GameKeys.UP, false);
		}
		else if(k == Keys.DOWN){
			GameKeys.setKey(GameKeys.DOWN, false);
		}
		else if(k == Keys.RIGHT){
			GameKeys.setKey(GameKeys.RIGHT, false);
		}
		else if(k == Keys.LEFT){
			GameKeys.setKey(GameKeys.LEFT, false);
		}
		else if(k == Keys.SPACE){
			GameKeys.setKey(GameKeys.SPACE, false);
		}
		return true;
	}

	@Override public boolean touchDown(int screenX, int screenY, int pointer, int button){

		/*if(screenX > Gdx.graphics.getWidth()/8 & screenX < Gdx.graphics.getWidth()/4 & Gdx.graphics.getHeight()*5/8 < screenY & screenY < Gdx.graphics.getHeight()*3/4){
			GameKeys.setKey(GameKeys.UP, true);
			System.out.println("Up");}
		else
		if(screenX > Gdx.graphics.getWidth()/8 & screenX < Gdx.graphics.getWidth()/4 & Gdx.graphics.getHeight()*7/8 < screenY ){
			GameKeys.setKey(GameKeys.DOWN, true);
			System.out.println("Down");}
		else
		if(screenX < Gdx.graphics.getWidth()/8 & Gdx.graphics.getHeight()*3/4 < screenY  & screenY < Gdx.graphics.getHeight()*7/8){
			GameKeys.setKey(GameKeys.LEFT, true);
			System.out.println("Left");}
		else
		if(screenX < Gdx.graphics.getWidth()*3/8 & screenX > Gdx.graphics.getWidth()/4 & screenX < Gdx.graphics.getWidth()*7/8 & Gdx.graphics.getHeight()*3/4 < screenY){
			GameKeys.setKey(GameKeys.RIGHT, true);
			System.out.println("Right");}
		else*/
		if(screenX> Gdx.graphics.getWidth()/2 & screenY > Gdx.graphics.getHeight()/2)
		{GameKeys.setKey(GameKeys.SPACE, true);
		point = pointer;}
		//System.out.println("X = " + screenX);
		//System.out.println("Y = " + screenY);
		bufX = screenX;
		bufY = screenY;

		return true;
	}

	@Override public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		if(screenX > Gdx.graphics.getWidth()/2 & screenY > Gdx.graphics.getHeight()/2)
		{}
		else
		{
			if(bufY - 15 > screenY & bufX - 15 < screenX & screenX < bufX + 15)
		{
			GameKeys.setKey(GameKeys.UP, true);
			GameKeys.setKey(GameKeys.DOWN, false);
			GameKeys.setKey(GameKeys.LEFT, false);
			GameKeys.setKey(GameKeys.RIGHT, false);
		}
		else
		if(bufY + 15 < screenY & bufX - 15 < screenX & screenX < bufX + 15)
		{
			GameKeys.setKey(GameKeys.DOWN, true);
			GameKeys.setKey(GameKeys.UP, false);
			GameKeys.setKey(GameKeys.LEFT, false);
			GameKeys.setKey(GameKeys.RIGHT, false);
		}
		else
		if(bufX - 15 > screenX & bufY - 15 < screenY & screenY < bufY + 15)
		{
			GameKeys.setKey(GameKeys.LEFT, true);
			GameKeys.setKey(GameKeys.UP, false);
			GameKeys.setKey(GameKeys.DOWN, false);
			GameKeys.setKey(GameKeys.RIGHT, false);
		}
		else
		if(bufX + 15 < screenX & bufY - 15 < screenY & screenY < bufY + 15)
		{
			GameKeys.setKey(GameKeys.RIGHT, true);
			GameKeys.setKey(GameKeys.UP, false);
			GameKeys.setKey(GameKeys.DOWN, false);
			GameKeys.setKey(GameKeys.LEFT, false);
		}
		else
		if(bufY - 15 < screenY & bufY + 15 > screenY & bufX - 15 < screenX & bufX + 15 > screenX)
		{
			GameKeys.setKey(GameKeys.RIGHT, false);
			GameKeys.setKey(GameKeys.UP, false);
			GameKeys.setKey(GameKeys.DOWN, false);
			GameKeys.setKey(GameKeys.LEFT, false);
		}
		point2 = pointer;
		}
		return true;
	}

	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button){

            if(pointer == point2)
			{
				GameKeys.setKey(GameKeys.UP, false);
				GameKeys.setKey(GameKeys.DOWN, false);
				GameKeys.setKey(GameKeys.LEFT, false);
				GameKeys.setKey(GameKeys.RIGHT, false);
                point2 = 0;
			}
        if(pointer == point)
        {
		    //if(screenX> Gdx.graphics.getWidth()/2 & screenY > Gdx.graphics.getHeight()/2)
			    GameKeys.setKey(GameKeys.SPACE, false);
        point = 0;}

		return true;
	}
}
