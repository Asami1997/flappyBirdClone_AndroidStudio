package com.asami.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.sun.net.ssl.internal.www.protocol.https.Handler;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	SpriteBatch batch;

	Texture gameOver;

	//ShapeRenderer shapeRenderer;

	//will overlap on the bird
	Circle circle ;

	Rectangle rectanglesDown[];

	Rectangle rectangleUp[];

	Texture background;

	Texture birds [];

	Texture tubeDown;

	Texture tubeUp;

	boolean collisionHappened;

	int flappyState = 0;

	int score;

	float birdYCoordinate = 0; //initially

	float birdVelocity =0;

	int gameState = 0;

	int scoringTube = 0;

	float gap = 400;

	float maxTubeHeightOffSet;

	Random randomGen;

	int numberOfTubes = 4; //4 sets of tubes

	float tubeVelocity =4;

	float tubeXCoordinate[] = new float[numberOfTubes];

	float tubeOffSet [] = new  float[numberOfTubes];  // will be added to the height to move it up or down

	float distanceBetweenTubes;


	BitmapFont font ;


	@Override
	public void create () {
		batch = new SpriteBatch();

		collisionHappened = false;

		gameOver = new Texture("gameover.png");

		font = new BitmapFont();

		font.setColor(Color.WHITE);

		font.getData().scale(10);

		// shapeRenderer = new ShapeRenderer();

	     circle = new Circle();

		rectanglesDown = new Rectangle[4];

		rectangleUp = new Rectangle[4] ;

		background = new Texture("bg.png"); //new background sprit

		birds = new Texture[2];     // new bird sprite

		birds[0] = new Texture("bird.png");

		birds[1] = new Texture("bird2.png");

	    tubeDown = new Texture("bottomtube.png");

		tubeUp = new Texture("toptube.png");

		// both birds has the same y position and same height , it dosent matter what you use
		//its y position on the screen

		//initially
		birdYCoordinate = Gdx.graphics.getHeight()/2-birds[0].getHeight()/2 ;

		maxTubeHeightOffSet = Gdx.graphics.getHeight() /2 - gap/2 - 100;

		randomGen = new Random();

		distanceBetweenTubes = Gdx.graphics.getWidth()/1.5f;
		//initially the x-Corrdinate of both tubes is the same

		//setting up the 4 sets tubes x corrdinate and offset just initially


		   //setting up the rectangles

		for(int y = 0 ; y < rectanglesDown.length ; y++)
		   {
			 rectanglesDown[y] = new Rectangle();

			   rectangleUp[y] = new Rectangle();
		   }

		   startGame();

	}


	public  void startGame()
	   {
		   //initially the x-Corrdinate of both tubes is the same

		   //setting up the 4 sets tubes x corrdinate and offset just initially

		   for(int i = 0; i< numberOfTubes ; i++){


			   tubeOffSet[i]= (randomGen.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap -200);
			   // will distance them apart thats why we are multiplying by i
			   tubeXCoordinate[i] =  Gdx.graphics.getWidth()/2 - tubeUp.getWidth()/2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes ;

		   }


		   //initially
		   birdYCoordinate = Gdx.graphics.getHeight()/2-birds[0].getHeight()/2 ;


	   }

	@Override
	public void render () {

		//deawing the background should be at the start otherwise , it will be on top of the tupes


		//this will detect if the screen has been taped

		//velocity will make the droping effect faster

		//if you decrease one every time only , it will look slow
		batch.begin(); //begin displaying sprites

		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()); //displaying the background sprite

		batch.end();

		if(gameState == 1)
		   {


			   //check every Tube as it moves, the 4 x coordinates
               if(tubeXCoordinate[scoringTube] < Gdx.graphics.getWidth()/2)
			      {
				      score++;

					  Gdx.app.log("Score",String.valueOf(score));

							  if(scoringTube < numberOfTubes -1 )
							  {
								  scoringTube++;
							  }else
							     {
								  scoringTube=0;
							     }
			      }



			   //y = 0 is the bottom of the screen
			   if(birdYCoordinate > 0 )
			      {
					  birdVelocity+=2;

					  birdYCoordinate-=birdVelocity; //decrease the y coordinate to let the bird drop
					  //if its touched duting the fall , increase the y coordinate
			      }else
			             {
                             gameState=2;
			              }



			   if(Gdx.input.justTouched())
			   {


				   //i need to get it upwards
				   //so increase the y coordinate

				   birdVelocity = -30;

				   birdYCoordinate-=birdVelocity;


			   }

			   for(int i =0 ; i < numberOfTubes ; i++){

				   //check if the tubes are off the screen

				   if(tubeXCoordinate[i] < - tubeUp.getWidth())
				      {
					   //we want to shift it `4 distances between tubes (4 half screen widths)

					   tubeXCoordinate[i] = numberOfTubes * distanceBetweenTubes;
						  //reset the random number for the four sets other wise , we will have same four locations for the pipes
						  tubeOffSet[i]= (randomGen.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap -200);

						  //dont move it 4 to left if this happend so it appears coming form the right end
				      }else{

                       // to get it moving

					   tubeXCoordinate[i] -= tubeVelocity;
				   }

				   // they dont overlap because of the disatnce between them in distancebetweentubes
				   batch.begin();
				   batch.draw(tubeUp,tubeXCoordinate[i],Gdx.graphics.getHeight()/2 + gap/2+ tubeOffSet[i],180,tubeUp.getHeight());

				   batch.draw(tubeDown,tubeXCoordinate[i],Gdx.graphics.getHeight()/2- gap/2 - tubeDown.getHeight()+tubeOffSet[i],180,tubeDown.getHeight());

				   batch.end();
				   //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

				   //shapeRenderer.setColor(Color.RED);

				   rectanglesDown[i].set(tubeXCoordinate[i] ,Gdx.graphics.getHeight()/2- gap/2 - tubeDown.getHeight()+tubeOffSet[i],180,tubeDown.getHeight());

				   rectangleUp[i].set(tubeXCoordinate[i] ,Gdx.graphics.getHeight()/2 + gap/2+ tubeOffSet[i],180,tubeUp.getHeight());

				   //shapeRenderer.rect(rectanglesDown[i].x,rectanglesDown[i].y,rectanglesDown[i].width,rectanglesDown[i].height);

				   //shapeRenderer.rect(rectangleUp[i].x,rectangleUp[i].y,rectangleUp[i].width,rectangleUp[i].height);

				   //shapeRenderer.end();
			   }




		   }else if(gameState==0)
		          {
			           if(Gdx.input.justTouched())
			              {
						    gameState=1;
					      }
				  }else if(gameState ==2)
		                     {
								 batch.begin();
								 batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2-100,Gdx.graphics.getHeight()/2,300,300);
								 batch.end();

								 //to make us interact with the screen otherwise it will be frozen since gamestate isnot changing
								 if(Gdx.input.justTouched())
								    {


										//set the tubes and bord positions to initial



										score = 0;

										birdVelocity =0;

										tubeVelocity =4;

										scoringTube =0;

										gameState =1;

										startGame();

							     	 }

							     	 Gdx.app.log("gameState",String.valueOf(gameState));
							 }

		if(flappyState == 0){

			flappyState = 1;
		}else
		{
			flappyState = 0;
		}




		batch.begin();

		batch.draw(birds[flappyState],Gdx.graphics.getWidth()/2-birds[flappyState].getWidth()/2,birdYCoordinate);

		//draw the font here to make sure ot in top of everything

		font.draw(batch,String.valueOf(score),100,200);

		batch.end();  //end displaying sprite

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		//shapeRenderer.setColor(Color.RED);

		// we add to the y coordinate because we want the center of the circle , not the bottom left of the bird , so we shift it back to the right a bit
		circle.set(Gdx.graphics.getWidth()/2,birdYCoordinate + birds[flappyState].getHeight()/2,birds[0].getWidth()/2);


		//hapeRenderer.circle(circle.x,circle.y,circle.radius);

		//shapeRenderer.end();

		//dedtecting collsions

		for(int i = 0 ; i < numberOfTubes ; i++)
		   {

			   //check that it overlaps with each of the 8 tubes
			   if(Intersector.overlaps(circle,rectangleUp[i])|| Intersector.overlaps(circle,rectanglesDown[i]))
			      {


					  collisionHappened = true;

					  gameState=2;


			      }
	     	}



	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
