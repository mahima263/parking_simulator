import java.util.*;
import java.util.concurrent.TimeUnit;

class Main {
    public static void main(String[] args) {
        
        ParkingCash cash = new ParkingCash(); 
        ParkingStats stats = new ParkingStats(cash);

        System.out.printf("Parking Simulator\n");
        
        int numberSensors=2 * Runtime.getRuntime()
                                           .availableProcessors(); 
        
        System.out.printf("No of sensor: "+numberSensors+"\n");
        
        Thread threads[]=new Thread[numberSensors]; 
        for (int i = 0; i<numberSensors; i++) { 
          Sensor sensor=new Sensor(stats); 
          Thread thread=new Thread(sensor); 
          thread.start(); 
          threads[i]=thread; 
        }
        
         for (int i=0; i<numberSensors; i++) { 
          try { 
            threads[i].join(); 
          } catch (InterruptedException e) { 
            e.printStackTrace(); 
          } 
        }
        
         System.out.printf("Number of cars: %d\n",
                              stats.getNumberCars()); 
            System.out.printf("Number of motorcycles: %d\n",
                               stats.getNumberMotorcycles()); 
      // Inconsistent result will produce as threads are not synchronized
            cash.close(); 
    }
}

class ParkingCash{
    private static final int cost = 2;
    private long cash;
    
    public ParkingCash(){
        cash =0;
    }
    
    public void vehiclePay() { 
          cash+=cost; 
    }
    
     public void close() { 
        System.out.printf("Closing accounting"); 
        long totalAmmount; 
        totalAmmount=cash; 
        cash=0; 
         System.out.printf("The total amount is : %d",
                              totalAmmount); 
     }          
}

class ParkingStats { 
    private long numberCars; 
    private long numberMotorcycles; 
    private ParkingCash cash;
    
    public ParkingStats(ParkingCash cash) { 
        numberCars = 0; 
        numberMotorcycles = 0; 
        this.cash = cash; 
    }
    
    public void carComeIn() { 
          numberCars++; 
    }
    
    public void carGoOut() { 
        numberCars--; 
        cash.vehiclePay(); 
    }
    
    public void motoComeIn() { 
          numberMotorcycles++; 
    }
    
    public void motoGoOut() { 
        numberMotorcycles--; 
        cash.vehiclePay(); 
    }
    
    public long getNumberMotorcycles() { 
        return  numberMotorcycles;
    }
    
    public long getNumberCars() { 
        return  numberCars;
    }
}

class Sensor implements Runnable {
    private ParkingStats stats;
    
      @Override 
        public void run() { 
          for (int i = 0; i< 10; i++) { 
            stats.carComeIn(); 
            stats.carComeIn(); 
            try { 
              TimeUnit.MILLISECONDS.sleep(50); 
            } catch (InterruptedException e) { 
              e.printStackTrace(); 
            } 
            stats.motoComeIn(); 
            try { 
              TimeUnit.MILLISECONDS.sleep(50); 
            } catch (InterruptedException e) { 
              e.printStackTrace(); 
            }
             stats.motoGoOut(); 
            stats.carGoOut(); 
            stats.carGoOut(); 
          } 
        }
    
    public Sensor(ParkingStats stats) { 
        this.stats = stats; 
    }
}


