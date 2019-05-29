#include <Servo.h>


int led=13;
Servo servoMotor;
int contador;
long time1;
long time2;
long delta;

void setup(){

  pinMode(led,OUTPUT);
  Serial.begin(9600);
  servoMotor.attach(9);
  contador = 0;
  time1 = 0;
  time2 = 0;
  delta =0;

}


int input = Serial.read();
void loop(){

  if(Serial.available()>0){
    //Empiece a contar, aumente el contador


    if(delta>=5000||delta ==0){

      int input = Serial.read();
      Serial.println(input);
      if(input ==49){

        digitalWrite(led, HIGH);   // turn the led on (HIGH is the voltage level)
        servoMotor.write(90);
        delay(4000);               // wait for a second
      }


    }      

  }

  servoMotor.write(0);
  delay(100); 
  time1 = millis(); 




}

