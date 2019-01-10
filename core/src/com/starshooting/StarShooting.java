package com.starshooting;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;

import java.util.Random;

import sun.rmi.runtime.Log;

public class StarShooting extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img,player,bullet,enemyR,enemyL,enemyC,enemyCC,boss,bossBullet,gameOver;
	int wigthP,wigthE,heightP,heightE,widthB,heightB;
	OrthographicCamera camera;
	int bulletH=-1, bulletW=-1,bulleBossW,bulletBossH;
	int bulletStat =0,bulletBossState=0;
	int dist =0;
	int velocityR,velocityL,velocityC,velocityCC,velocityBoss;
	int enemyLeftX,enemyLeftY,enemyRightX,enemyRightY,enemyCenterX,enemyCenterY,bossX,bossY;
	Random ran;
	int score =0,damage=0;
	ShapeRenderer shapeRanderer;
	BitmapFont font;
	Rectangle playerRect,enemyRectR,playerB,enemyB,enemyRectL,enemyRectC,enemyRectCC,bossRect;
	Rectangle bulletRect,bossBulletRect;
	int k=0,gameStat=0;
	Sound gun,explosion;
    Music space;
	boolean bossState = false;
	boolean toRight=true,toLeft=false;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
		camera.update(); // Updates the camera
		batch = new SpriteBatch();
		img = new Texture("bg.png");
		player = new Texture("player.png");
		ran = new Random();
		enemyR = new Texture("enemy.png");
		enemyL = new Texture("enemy.png");
		enemyC = new Texture("enemy.png");
		enemyCC = new Texture("enemy.png");
		gameOver= new Texture("gameover.png");
		bullet = new Texture("b1.png");
		bulletRect = new Rectangle();

		wigthP = (int) (player.getWidth()*0.75);
		heightP = (int) (player.getHeight()*0.75);

		wigthE = (int) (enemyR.getWidth()*0.50);
		heightE = (int) (enemyR.getHeight()*0.50);

		widthB = (int) (bullet.getWidth()*0.25);
		heightB = (int) (bullet.getHeight()*0.20);

		shapeRanderer = new ShapeRenderer();
		playerRect = new Rectangle();
		enemyRectR = new Rectangle();
		enemyRectL = new Rectangle();
		enemyRectC = new Rectangle();
		gun = Gdx.audio.newSound(Gdx.files.internal("laser.mp3"));
		explosion = Gdx.audio.newSound(Gdx.files.internal("Explosion.wav"));
		int s = ran.nextInt(5);
		if(s==1)
		{
			space = Gdx.audio.newMusic(Gdx.files.internal("space2.mp3"));
		}
		else if(s==2)
		{
			space = Gdx.audio.newMusic(Gdx.files.internal("space.mp3"));
		}
		else if(s==3)
		{
			space = Gdx.audio.newMusic(Gdx.files.internal("space1.mp3"));
		}
		else if(s==0)
		{
			space = Gdx.audio.newMusic(Gdx.files.internal("space3.mp3"));
		}
        else if(s==4)
        {
            space = Gdx.audio.newMusic(Gdx.files.internal("klay.mp3"));
        }


        space.play();



		boss = new Texture("enemy2.png");
		bossBullet = new Texture("b2.png");
		bossRect = new Rectangle();
		bossBulletRect = new Rectangle();
		velocityBoss = 4;

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(5);

bulletH=-1;
		bulletW=-1;

		velocityR = ran.nextInt(10);
		velocityL = ran.nextInt(10);
		velocityC = ran.nextInt(10);
		velocityCC = ran.nextInt(10);

	}

	@Override
	public void render () {


		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (Gdx.input.justTouched() || gameStat ==1) {
			if (gameStat == 0) {
				initPosEnemy();
				score =0;
				damage =0;
				bossState = false;
				bossY=1000;
				bossX=1000;
				bulletRect.set(Gdx.graphics.getHeight()+600,Gdx.graphics.getWidth()+ 600, heightB, widthB);
				gameStat = 1;
			}

			if (score <= 20) {

				resetEnemy();
				enemyLeftY -= velocityL;
				enemyCenterY -= velocityC;
				enemyRightY -= velocityR;
			} else {
				resetEnemy();
				if (bossState == false) {

					bossX = -75 + Gdx.graphics.getWidth() / 2;
					bossY = Gdx.graphics.getHeight() - ran.nextInt(300);

					velocityBoss = ran.nextInt(10);
					bossState = true;
				} else {


					batch.draw(boss, bossX, bossY, heightE * 2, wigthE * 2);
					bossRect.set(bossX, bossY, heightE * 2, wigthE * 2);

					if (bossY > Gdx.graphics.getHeight() - heightE * 2) {
						bossY -= velocityBoss;
					} else {

						if (bulletBossState == 0) {

							//gun.play();

							bulletBossState = 1;
							bulletBossH = (int) (bossY);
							//batch.draw(bullet,(int) (touchX+bullet.getWidth()/2.4),bulletH,widthB,heightB);
							bulleBossW = (int) (bossX * 1.5);
							batch.draw(bossBullet, bulleBossW, bulletBossH, widthB * 2, heightB * 2);
						} else {
							bulletBossH = bulletBossH - 15;

							batch.draw(bossBullet, bulleBossW, bulletBossH, widthB * 2, heightB * 2);
							bossBulletRect.set(bulleBossW, bulletBossH, heightB * 2, widthB * 2);

							//dist += 35;

						}

						if (bossX <= Gdx.graphics.getWidth() - wigthE * 2 && toRight == true) {
							bossX += velocityBoss;
						} else {
							toRight = false;
							toLeft = true;
						}

						if (bossX > 0 && toLeft == true) {
							bossX -= velocityBoss;
						} else {
							toRight = true;
							toLeft = false;
						}


					}

				}
			}


			//batch.draw(player, Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,wigthP,heightP);



		/*batch.draw(enemy1,Gdx.graphics.getHeight()-heightE,0,wigthE,heightE);
		batch.draw(enemy2,Gdx.graphics.getHeight()-heightE,wigthE+10,wigthE,heightE);
		batch.draw(enemy3,Gdx.graphics.getHeight()-heightE,wigthE*2+10,wigthE,heightE);
		batch.draw(enemy4,Gdx.graphics.getHeight()-heightE,wigthE*3+10,wigthE,heightE);*/

			//enemyRect.set(i,j,wigthE,heightE);


			//batch.draw(enemyL,enemyLeftX,enemyLeftY,wigthE,heightE);

			int touchX = 0;//=Gdx.input.getX();
			int touchY = 0;//=Gdx.input.getY();

			if (bulletStat == 0) {
				if (Gdx.input.justTouched() || bulletStat == 0) {

					gun.play();
					touchX = Gdx.input.getX();
					touchY = Gdx.input.getY();
					bulletStat = 1;
					bulletH = Gdx.graphics.getHeight() -
							touchY + player.getHeight() / 2;
					//batch.draw(bullet,(int) (touchX+bullet.getWidth()/2.4),bulletH,widthB,heightB);
					bulletW = (int)
							(touchX + bullet.getWidth() / 2.4);
				}

			} else {
				bulletH = bulletH + 25;

				batch.draw(bullet, bulletW, bulletH, widthB, heightB);
				bulletRect.set(bulletW, bulletH, heightB, widthB);

				dist += 35;

			}

			batch.draw(enemyL, enemyLeftX, enemyLeftY, heightE, wigthE);
			batch.draw(enemyR, enemyRightX, enemyRightY, heightE, wigthE);
			batch.draw(enemyC, enemyCenterX, enemyCenterY, heightE, wigthE);

			touchX = Gdx.input.getX();
			touchY = Gdx.input.getY();
			batch.draw(player, touchX, Gdx.graphics.getHeight() - touchY, wigthP, heightP);
			playerRect.set(touchX, Gdx.graphics.getHeight() - touchY, heightP, wigthP);


			if (bulletH > Gdx.graphics.getHeight())
				bulletStat = 0;
			if (bulletBossH < 0)
				bulletBossState = 0;
			//bulletH = bulletH+ 15;

			//if(Intersector.overlaps(playerB,enemyRect))

			//shapeRanderer.begin(ShapeRenderer.ShapeType.Filled);
			//shapeRanderer.setColor(Color.RED);


			//shapeRanderer.rect(bulletRect.x,bulletRect.y,bulletH,bulletW);

			//shapeRanderer.rect(playerRect.x,playerRect.y,heightP,wigthP);

			enemyRectR.set(enemyRightX, enemyRightY, heightE, wigthE);
			//shapeRanderer.rect(enemyRectR.x,enemyRectR.y,heightE,wigthE);

			enemyRectL.set(enemyLeftX, enemyLeftY, heightE, wigthE);
			//shapeRanderer.rect(enemyRectL.x,enemyRectL.y,heightE,wigthE);

			enemyRectC.set(enemyCenterX, enemyCenterY, heightE, wigthE);
			//shapeRanderer.rect(enemyRectC.x,enemyRectC.y,heightE,wigthE);

//			shapeRanderer.rect(bossRect.x,bossRect.y,heightE*2,wigthE*2);

//shapeRanderer.end();
			font.draw(batch,String.valueOf(score),100,100);
			if (enemyRectR.overlaps(bulletRect)) {

				enemyRightY = Gdx.graphics.getHeight() + ran.nextInt(500);
				bulletStat = 0;
				score++;
				explosion.play();

				do {
					velocityR = ran.nextInt(10);
				} while (velocityR == 0);
			}

			if (enemyRectL.overlaps(bulletRect)) {

				enemyLeftY = Gdx.graphics.getHeight() + ran.nextInt(500);
				bulletStat = 0;
				score++;
				explosion.play();
				do {
					velocityL = ran.nextInt(10);
				} while (velocityL == 0);
			}

			if (enemyRectC.overlaps(bulletRect)) {

				enemyCenterY = Gdx.graphics.getHeight() + ran.nextInt(500);
				bulletStat = 0;
				score++;
				explosion.play();
				do {
					velocityC = ran.nextInt(10);
				} while (velocityC == 0);
			}
			//System.out.println("X ==== "+touchX);
			//System.out.println("Y ==== "+touchY);

			if (playerRect.overlaps(bossBulletRect)) {

				//gun.dispose();
				batch.draw(gameOver, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				bossY=1000;
				bossX=1000;
				damage =0;
				gameStat = 0;
			}

			if (bossRect.overlaps(bulletRect)) {

				if(bulletStat ==1)
					damage++;

				bulletStat = 0;


				System.out.println("boss HITTEN");
				explosion.play();

			}

			if (damage == 10) {
				//explosion.dispose();
				//gun.dispose();
				bossY=1000;
				bossX=1000;
				damage =0;
				System.out.println("BOSS DEAD");
				gameStat = 0;
				batch.draw(gameOver, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			}

		}
			batch.end();

	}
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public  void initPosEnemy(){

		enemyLeftX=-300+Gdx.graphics.getWidth()/2;
		enemyLeftY = Gdx.graphics.getHeight()-ran.nextInt(300);

		enemyRightX= 150+Gdx.graphics.getWidth()/2;
		enemyRightY = Gdx.graphics.getHeight()-ran.nextInt(300);

		enemyCenterX= -75+Gdx.graphics.getWidth()/2;
		enemyCenterY = Gdx.graphics.getHeight()-ran.nextInt(300);

		velocityR = ran.nextInt(10);
		velocityL = ran.nextInt(10);
		velocityC = ran.nextInt(10);

	}

	public void resetEnemy(){

		if (enemyLeftY < -610) {
			enemyLeftY = Gdx.graphics.getHeight() + ran.nextInt(500);
			score --;
			do {
				velocityL = ran.nextInt(10);
			} while (velocityL == 0);

		}

		if (enemyCenterY < -610) {
			enemyCenterY = Gdx.graphics.getHeight() + ran.nextInt(500);
			score --;
			do {
				velocityL = ran.nextInt(10);
			} while (velocityC == 0);

		}

		if (enemyRightY < -610) {
			enemyRightY = Gdx.graphics.getHeight() + ran.nextInt(500);
			score --;
			do {
				velocityR = ran.nextInt(10);
			} while (velocityR == 0);

		}


	}

}
