package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;


public class InputProcessor extends InputAdapter {

	public static int bufX;
	public static int bufY;
	public static int point = 0;
	public static int point2 = 0;
	public static int arrowX;
	public static int arrowY;
	public static boolean onAr = false;
	public static int ar = 0;

	@Override

	public boolean keyDown(int k) {

		if (k == Keys.UP) {
			GameKeys.setKey(GameKeys.UP, true);
		} else if (k == Keys.DOWN) {
			GameKeys.setKey(GameKeys.DOWN, true);
		} else if (k == Keys.RIGHT) {
			GameKeys.setKey(GameKeys.RIGHT, true);
		} else if (k == Keys.LEFT) {
			GameKeys.setKey(GameKeys.LEFT, true);
		} else if (k == Keys.SPACE) {
			GameKeys.setKey(GameKeys.SPACE, true);
		} else if (k == Keys.NUM_1)
			GameKeys.setKey(GameKeys.GET_POS, true);
		return true;
	}

	@Override
	public boolean keyUp(int k) {
		if (k == Keys.UP) {
			GameKeys.setKey(GameKeys.UP, false);
		} else if (k == Keys.DOWN) {
			GameKeys.setKey(GameKeys.DOWN, false);
		} else if (k == Keys.RIGHT) {
			GameKeys.setKey(GameKeys.RIGHT, false);
		} else if (k == Keys.LEFT) {
			GameKeys.setKey(GameKeys.LEFT, false);
		} else if (k == Keys.SPACE) {
			GameKeys.setKey(GameKeys.SPACE, false);
		} else if (k == Keys.NUM_1)
			GameKeys.setKey(GameKeys.GET_POS, false);
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

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
		if (screenX > Gdx.graphics.getWidth() / 2 & screenY > Gdx.graphics.getHeight() / 2) {
			GameKeys.setKey(GameKeys.SPACE, true);
			point = pointer;
		}

		if (screenX > Gdx.graphics.getWidth() / 2 & screenY > Gdx.graphics.getHeight() / 2) {
		} else {
			onAr = true;
			if (ar == 0) {
				arrowX = screenX;
				arrowY = screenY;
				ar = 1;
			} else {
				ar++;
			}
		}

		bufX = screenX;
		bufY = screenY;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (screenX > Gdx.graphics.getWidth() / 2 & screenY > Gdx.graphics.getHeight() / 2) {
		} else {
			onAr = true;
			double Ax = 0;
			double Ay = arrowY;

			double a = Math.sqrt(Math.pow((screenX - arrowX), 2) + Math.pow((screenY - arrowY), 2));
			double b = Math.sqrt(Math.pow((Ax - screenX), 2) + Math.pow((Ay - screenY), 2));
			double c = Math.sqrt(Math.pow((arrowX - Ax), 2) + Math.pow((arrowY - Ay), 2));
			double angle = (Math.pow(a, 2) + Math.pow(c, 2) - Math.pow(b, 2)) / (2 * c * a);
			angle = Math.acos(angle) * 180;
			angle /= Math.PI;
			if (screenY > arrowY)
				angle = 360 - angle;

			if (angle > 45 & angle < 135) {
				GameKeys.setKey(GameKeys.UP, true);
				GameKeys.setKey(GameKeys.DOWN, false);
				GameKeys.setKey(GameKeys.LEFT, false);
				GameKeys.setKey(GameKeys.RIGHT, false);
			} else if (angle > 225 & angle < 315) {
				GameKeys.setKey(GameKeys.DOWN, true);
				GameKeys.setKey(GameKeys.UP, false);
				GameKeys.setKey(GameKeys.LEFT, false);
				GameKeys.setKey(GameKeys.RIGHT, false);
			} else if ((angle > 315 & angle <= 360) | (angle > 0 & angle < 45)) {
				GameKeys.setKey(GameKeys.LEFT, true);
				GameKeys.setKey(GameKeys.UP, false);
				GameKeys.setKey(GameKeys.DOWN, false);
				GameKeys.setKey(GameKeys.RIGHT, false);
			} else if (angle > 135 & angle < 225) {
				GameKeys.setKey(GameKeys.RIGHT, true);
				GameKeys.setKey(GameKeys.UP, false);
				GameKeys.setKey(GameKeys.DOWN, false);
				GameKeys.setKey(GameKeys.LEFT, false);
			}

			/*if (arrowY - 15 > screenY & arrowX - 15 < screenX & screenX < arrowX + 15) {
				GameKeys.setKey(GameKeys.UP, true);
				GameKeys.setKey(GameKeys.DOWN, false);
				GameKeys.setKey(GameKeys.LEFT, false);
				GameKeys.setKey(GameKeys.RIGHT, false);
			} else if (arrowY + 15 < screenY & arrowX - 15 < screenX & screenX < arrowX + 15) {
				GameKeys.setKey(GameKeys.DOWN, true);
				GameKeys.setKey(GameKeys.UP, false);
				GameKeys.setKey(GameKeys.LEFT, false);
				GameKeys.setKey(GameKeys.RIGHT, false);
			} else if (arrowX - 15 > screenX & arrowY - 15 < screenY & screenY < arrowY + 15) {
				GameKeys.setKey(GameKeys.LEFT, true);
				GameKeys.setKey(GameKeys.UP, false);
				GameKeys.setKey(GameKeys.DOWN, false);
				GameKeys.setKey(GameKeys.RIGHT, false);
			} else if (arrowX + 15 < screenX & arrowY - 15 < screenY & screenY < arrowY + 15) {
				GameKeys.setKey(GameKeys.RIGHT, true);
				GameKeys.setKey(GameKeys.UP, false);
				GameKeys.setKey(GameKeys.DOWN, false);
				GameKeys.setKey(GameKeys.LEFT, false);
			} else if (arrowY - 15 < screenY & arrowY + 15 > screenY & arrowX - 15 < screenX & arrowX + 15 > screenX) {
				GameKeys.setKey(GameKeys.RIGHT, false);
				GameKeys.setKey(GameKeys.UP, false);
				GameKeys.setKey(GameKeys.DOWN, false);
				GameKeys.setKey(GameKeys.LEFT, false);
			}*/
			point2 = pointer;

		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		if (pointer == point2) {
			GameKeys.setKey(GameKeys.UP, false);
			GameKeys.setKey(GameKeys.DOWN, false);
			GameKeys.setKey(GameKeys.LEFT, false);
			GameKeys.setKey(GameKeys.RIGHT, false);
			if (ar == 1) {
				onAr = false;
				ar = 0;
			} else
				ar--;
			point2 = 0;
		}
		if (pointer == point) {
			//if(screenX> Gdx.graphics.getWidth()/2 & screenY > Gdx.graphics.getHeight()/2)
			GameKeys.setKey(GameKeys.SPACE, false);
			point = 0;
		}

		return true;
	}
}
