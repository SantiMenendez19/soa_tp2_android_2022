// REGION LIBRARY
#include <IRremote.h> //sensor IR
#include <SoftwareSerial.h> //Seriar For bluetooth
// END REGION LIBRARY

// REGION PIN
#define PIN_OPEN_SWITCH 3
#define PIN_CLOSE_SWITCH 13
#define PIN_GREEN_LED 5
#define PIN_RED_LED 4
#define PIN_BUTTON 2
#define PIN_ENGINE_OPEN 11
#define PIN_ENGINE_CLOSE 10
#define PIN_ACTIVATE_ENGINE 12
#define PIN_REMOTE_CONTROL_IR 6
#define PIN_TX_BT_RX 8
#define PIN_RX_BT_TX 9
#define PIN_POWER_METER A1
// END REGION PIN

// REGION STATE
#define STATE_INITIAL 0
#define STATE_OPENED 1
#define STATE_CLOSED 2
#define STATE_OPENING 3
#define STATE_CLOSING 4
#define STATE_STOP_OPENING 5
#define STATE_STOP_CLOSING 6
// END REGION STATE

// REGION EVENT
#define EVENT_AUTOMATIC_ACTION_CLOSE 1
#define EVENT_AUTOMATIC_ACTION_OPEN 2
#define EVENT_MANUAL_ACTION 3
#define EVENT_NOT_MODIFIED 4
// END REGION EVENT

// REGION DIRECTION ENGINE
#define DIRECTION_CLOSE_ENGINE -1
#define DIRECTION_STOP_ENGINE 0
#define DIRECTION_OPEN_ENGINE 1
// REGION DIRECTION ENGINE

//REGION BLUETOOTH FLAGS
#define FLAG_INFO_UPPER 'I'
#define FLAG_INFO_LOWER 'i'
#define FLAG_ACTION_UPPER 'A'
#define FLAG_ACTION_LOWER 'a'
//END REGION BLUETOOTH FLAGS

// REGION OTHERS
#define DEFAULT_BAUND_RATE 9600
#define ARRAY_LENGTH 2
#define POWER_TRANSFORM 4
#define INDEX_ZERO 0
#define INDEX_ONE 1 
// END REGION OTHERS

//REGION INIT SOFTWARE SERIAL
SoftwareSerial bt(PIN_RX_BT_TX ,PIN_TX_BT_RX);
//END REGION INIT SOFTWARE SERIAL


// REGION VARIABLES
char btFlag="";
int powerVal=0;
int power = 0;
int state = STATE_INITIAL;
int event = 0;
int previusEvent = LOW;
int buttonPush = LOW;
int buttonRemote = LOW;
int finalOpen = LOW;
int finalClosed = HIGH;
int remoteCode = -1;
int btAction =LOW;
//INDICES DE ARRAYS DE SENSORES DE FIN DE CARRERA 
int indexSwitchSensors[ARRAY_LENGTH]={INDEX_ZERO,INDEX_ONE};
// END REGION VARIABLES


void initInputs()
{
  pinMode(PIN_OPEN_SWITCH, INPUT);
  pinMode(PIN_CLOSE_SWITCH, INPUT);
  pinMode(PIN_BUTTON, INPUT);
  pinMode(PIN_REMOTE_CONTROL_IR, INPUT);
  IrReceiver.begin(PIN_REMOTE_CONTROL_IR, ENABLE_LED_FEEDBACK);
  pinMode(PIN_POWER_METER, INPUT);
  pinMode(PIN_RX_BT_TX, INPUT);
}

void initOutputs()
{
  pinMode(PIN_GREEN_LED, OUTPUT);
  pinMode(PIN_RED_LED, OUTPUT);
  pinMode(PIN_ENGINE_OPEN, OUTPUT);
  pinMode(PIN_ENGINE_CLOSE, OUTPUT);
  pinMode(PIN_ACTIVATE_ENGINE, OUTPUT);
  pinMode(PIN_TX_BT_RX, OUTPUT);
  
}

//INTERCAMBIA EL ORDEN DE LOS INDICES DE ARRAY DE SENSORES DE FIN DE CARRERA 
void reverseIndex()
{
  int aux = indexSwitchSensors[INDEX_ZERO];
  indexSwitchSensors[INDEX_ZERO] = indexSwitchSensors[INDEX_ONE];
  indexSwitchSensors[INDEX_ONE] = aux;
}

//DEFINE EVENTE SEGUN SENSORES DE FINALE DE CARRERA PERO ALTERNA EL ORDEN EN QUE LOS LEE EN CADA LOOP 
int alternateSwitchEvent()
{
  reverseIndex();
  int sensorResult[ARRAY_LENGTH] ={finalClosed,finalOpen};
  int sensorEvent[ARRAY_LENGTH] = {EVENT_AUTOMATIC_ACTION_CLOSE,EVENT_AUTOMATIC_ACTION_OPEN};

  int index = indexSwitchSensors[INDEX_ZERO];
  if(sensorResult[index]==HIGH)
  {
    return sensorEvent[index];
  }
  index = indexSwitchSensors[INDEX_ONE];
  if(sensorResult[index]==HIGH)
  {
    return sensorEvent[index];
  }
   return LOW;
}
int defineEvent()
{
    int oldManualActionPush = buttonPush;
    int oldManualActionRemote = buttonRemote;
    int oldBtAction = btAction;
    readSensors();
    readBluetooh();
    if((buttonPush == HIGH && oldManualActionPush==LOW) 
    || (buttonRemote == HIGH && oldManualActionRemote==LOW)
    || (btAction == HIGH && oldBtAction==LOW)){
        return EVENT_MANUAL_ACTION;
    }
    int sensorEvent = alternateSwitchEvent();
    if(sensorEvent > LOW)
    {
        return sensorEvent;
    }
    return EVENT_NOT_MODIFIED;
}

void turnLight(int pinLedOn,int pinLedOff) {
    digitalWrite(pinLedOff, LOW);
    digitalWrite(pinLedOn, HIGH);
    
}

void moveEngine(int direction)
{
  if (direction == DIRECTION_STOP_ENGINE)
  {
    digitalWrite(PIN_ACTIVATE_ENGINE,LOW);
    digitalWrite(PIN_ENGINE_CLOSE,LOW);
    digitalWrite(PIN_ENGINE_OPEN,LOW);
    
  }
  if (direction == DIRECTION_OPEN_ENGINE)
  {
    
    digitalWrite(PIN_ACTIVATE_ENGINE,HIGH);
    digitalWrite(PIN_ENGINE_CLOSE,LOW);
    analogWrite(PIN_ENGINE_OPEN,power);
  }
  if (direction == DIRECTION_CLOSE_ENGINE)
  {
    
    digitalWrite(PIN_ACTIVATE_ENGINE,HIGH);
    digitalWrite(PIN_ENGINE_OPEN,LOW);
    analogWrite(PIN_ENGINE_CLOSE,power);
    
  }
}

int readRemote()
{
  int value = LOW;
  int oldRemoteCode = remoteCode;
  if (IrReceiver.decode())
  {
    remoteCode =IrReceiver.decodedIRData.command;
    IrReceiver.resume();
  }
  else{
    remoteCode =-1;
  }
  if(oldRemoteCode!=remoteCode)
  {
    value = HIGH;
  }
  return value;
}

void readSensors() {
    // Pulsador
    int buttonVal = digitalRead(PIN_BUTTON);
    buttonPush = buttonVal;
    // Control Remoto
    int remoteVal = readRemote();
    buttonRemote = remoteVal;
    // Potenciometro
    powerVal = analogRead(PIN_POWER_METER);
    power = powerVal / POWER_TRANSFORM;
    // Fin de carrera cierre
    int closedSwitchVal = digitalRead(PIN_CLOSE_SWITCH);
    finalClosed = closedSwitchVal;
    // Fin de carrera apertura
    int openSwitchVal = digitalRead(PIN_OPEN_SWITCH);
    finalOpen = openSwitchVal;
}

void readBluetooh() {
  btFlag ="";
  if (bt.available()> 0){   
    btFlag = bt.read(); 
  }
btAction =  LOW;
  switch(btFlag){
      case FLAG_INFO_UPPER:
      case FLAG_INFO_LOWER:
        bt.println(powerVal);
        break;
      case FLAG_ACTION_UPPER:
      case FLAG_ACTION_LOWER:
        btAction =HIGH; 
        break;
      default:
        break;                
    }
}
template <typename T>
void logAction(T message)
{
    if(event != previusEvent)
    {
      Serial.println(message);
    }
}

void stateMachine()
{
    previusEvent = event;
    event = defineEvent();
    switch (state)
    {
        case STATE_INITIAL:
        switch(event)
            {
                case EVENT_AUTOMATIC_ACTION_CLOSE:
                    turnLight(PIN_GREEN_LED,PIN_RED_LED);
                    moveEngine(DIRECTION_STOP_ENGINE);
                    state = STATE_CLOSED;
                    logAction("Closed");
                    break;
                case EVENT_AUTOMATIC_ACTION_OPEN:
                    turnLight(PIN_RED_LED,PIN_GREEN_LED);
                    moveEngine(DIRECTION_STOP_ENGINE);
                    state = STATE_OPENED;
                    logAction("Opened");
                    break;
                case EVENT_MANUAL_ACTION:
                    moveEngine(DIRECTION_CLOSE_ENGINE);
                    turnLight(PIN_RED_LED,PIN_GREEN_LED);
                    logAction("Closing...");
                    state = STATE_CLOSING;
                    break;
                case EVENT_NOT_MODIFIED:
                    turnLight(PIN_RED_LED,PIN_GREEN_LED);
                    moveEngine(DIRECTION_STOP_ENGINE);
                    logAction("Stop Opening");
                    state = STATE_STOP_OPENING;
                    break;
                default:
                    logAction("Invalid Event");
                    break;
            }
            break;
        case STATE_CLOSED:
        switch (event)
        {
            case EVENT_AUTOMATIC_ACTION_CLOSE:
                // NOTHING TODO
                break;
            case EVENT_AUTOMATIC_ACTION_OPEN:
                logAction("Check Open sensor failure");
                break;
            case EVENT_MANUAL_ACTION:
                moveEngine(DIRECTION_OPEN_ENGINE);
                turnLight(PIN_RED_LED,PIN_GREEN_LED);
                logAction("Opening..");
                state = STATE_OPENING;
                break;
            case EVENT_NOT_MODIFIED:
                logAction("Check Close sensor failure");
                break;
            default:
                logAction("Invalid Event");
                break;
        }
            break;
    case STATE_OPENED:
        switch (event)
        {
            case EVENT_AUTOMATIC_ACTION_CLOSE:
                logAction("Check Close sensor failure");
                break;
            case EVENT_AUTOMATIC_ACTION_OPEN:
                // NOTHING TODO
                break;
            case EVENT_MANUAL_ACTION:
                moveEngine(DIRECTION_CLOSE_ENGINE);
                logAction("Closing..");
                state = STATE_CLOSING;
                break;
            case EVENT_NOT_MODIFIED:
                logAction("Check Open sensor failure");
                break;
            default:
                logAction("Invalid Event");
                break;
        }
            break;
    case STATE_OPENING:
        switch (event)
        {
            case EVENT_AUTOMATIC_ACTION_CLOSE:
                logAction("Check Close sensor failure");
                break;
            case EVENT_AUTOMATIC_ACTION_OPEN:
                moveEngine(DIRECTION_STOP_ENGINE);
                logAction("Open");
                state = STATE_OPENED;
                break;
            case EVENT_MANUAL_ACTION:
                moveEngine(DIRECTION_STOP_ENGINE);
                logAction("Stop Opening");
                state = STATE_STOP_OPENING;
                break;
            case EVENT_NOT_MODIFIED:
                moveEngine(DIRECTION_OPEN_ENGINE);
                break;
            default:
                logAction("Invalid Event");
                break;
        }
            break;
        case STATE_STOP_OPENING:
        switch (event)
        {
            case EVENT_AUTOMATIC_ACTION_CLOSE:
            logAction("Check Close sensor failure");
            break;
            case EVENT_AUTOMATIC_ACTION_OPEN:
            logAction("Check Open sensor failure");
            break;
            case EVENT_MANUAL_ACTION:
            moveEngine(DIRECTION_CLOSE_ENGINE);
            logAction("Closing..");
            state = STATE_CLOSING;
            break;
            case EVENT_NOT_MODIFIED:
            // NOTHING TODO
            break;
        default:
            logAction("Invalid Event");
            break;
        }
            break;
    case STATE_CLOSING:
        switch (event)
        {
        case EVENT_AUTOMATIC_ACTION_CLOSE:
            moveEngine(DIRECTION_STOP_ENGINE);
            turnLight(PIN_GREEN_LED,PIN_RED_LED);
            logAction("Close");
            state = STATE_CLOSED;
            break;
        case EVENT_AUTOMATIC_ACTION_OPEN:
            logAction("Check Open sensor failure");
            break;
        case EVENT_MANUAL_ACTION:
            moveEngine(DIRECTION_STOP_ENGINE);
            logAction("Stop Closing");
            state = STATE_STOP_CLOSING;
            break;
        case EVENT_NOT_MODIFIED:
            moveEngine(DIRECTION_CLOSE_ENGINE);
            break;
        default:
            logAction("Invalid Event");
            break;
        }
            break;
    case STATE_STOP_CLOSING:
        switch (event)
        {
            case EVENT_AUTOMATIC_ACTION_CLOSE:
                logAction("Check Close sensor failure");
                break;
            case EVENT_AUTOMATIC_ACTION_OPEN:
                logAction("Check Open sensor failure");
                break;
            case EVENT_MANUAL_ACTION:
                moveEngine(DIRECTION_OPEN_ENGINE);
                logAction("Opening..");
                state = STATE_OPENING;
                break;
            case EVENT_NOT_MODIFIED:
                // NOTHING TODO
                break;
            default:
                logAction("Invalid Event");
                break;
            }
            break;
        default:
            logAction("Invalid State");
            break;
    }
}

void setup()
{
  Serial.begin(DEFAULT_BAUND_RATE);
  initInputs();
  initOutputs();
  bt.begin(DEFAULT_BAUND_RATE);
}

void loop()
{
    stateMachine();
}
