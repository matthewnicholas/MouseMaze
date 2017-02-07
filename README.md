# MouseMaze
Initial Commit - gitignore
This is the one i was working on
Everything already works here up to 3 mice i have added- i was just adding details at this point like the mice "smelling" each other and i need to add a bias in the random direction picking (ie if you cant continue moving the direction you are going you are more likely to turn left or right instead of all the way around) right now if you cant move forward you preferedDirection = (int)Math.random()*4+1; which will equally choose between the 4 directions and i think this is a semantic falicy
