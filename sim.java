import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.OutputStreamWriter;

class sim {
    public static void main(String[] args) {
        switch (args[0]) {
            case "smith":
                // initialize values
                int B = args[1];
                String tracefileString = args[2];

                // validate inputs
                //if (B <= 0 || tracefileString == null);

                // open file
                BufferedReader fileReader = new BufferedReader(new FileReader(tracefileString));

                // initialize accesses and mispredictions
                int accesses = 0;
                int mispredictions = 0;
                String lineString;
                smithPredictor SP = new smithPredictor(1 << B);

                // iterate through trace
                while ((lineString = fileReader.readLine()) != null) {
                    // split line
                    String[] components = lineString.split(" ");
                    boolean actualResult = String[1].equals("t");

                    // get result from predictor
                    boolean predictedResult = SP.call();
                    SP.update(actualResult);

                    accesses++;
                    if (predictedResult != actualResult)
                        mispredictions++;
                }

                BufferedWriter myWriter = new BufferedWriter(new OutputStreamWriter(System.out));
                myWriter.write("COMMAND\n");
                myWriter.write("./sim smith " + B + " " + tracefileString + "\n");
                myWriter.write("OUTPUT\n");
                myWriter.write("number of predictions:\t" + accesses + "\n");
                myWriter.write("number of mispredictions:\t" + mispredictions + "\n");
                myWriter.write("misprediction rate:\t" + String.format("%.2f", (float) mispredictions / accesses) + "%\n");
                myWriter.write("FINAL COUNTER CONTENT:\t");
                myWriter.write(SP.print());

                break;

            case "bimodal":
                // initialize values
                // initialize accesses and mispredictions
                // iterate through trace
                    // split line
                    // get result from predictor
                    // accessess++
                    // if (result != actualResult) mispredictions++;
                // print results
                    // bufferedprint(this.print())
                    // bufferedprint(predictor.print())
                break;

            case "gshare":
                // initialize values
                // initialize accesses and mispredictions
                // iterate through trace
                    // split line
                    // get result from predictor
                    // accessess++
                    // if (result != actualResult) mispredictions++;
                // print results
                    // bufferedprint(this.print())
                    // bufferedprint(predictor.print())
                break;

            case "hybrid":
                // initialize values
                // initialize accesses and mispredictions
                // iterate through trace
                    // split line
                    // get result from predictor
                    // accessess++
                    // if (result != actualResult) mispredictions++;
                // print results
                    // bufferedprint(this.print())
                    // bufferedprint(predictor.print())
                break;
        }
    }

    public String print() {
        // COMMAND
        // "./sim " + args
        // OUTPUT
        // "number of predictions:\t" + accessess
        // "number of mispredictions:\t" + mispredictions
        // "misprediction rate:\t" + .2f(mispredictions/accessess) + "%\n"
        // Maybe return a buffered string thing?
    }
}

class basicPredictor {
    public int max, min, current, flip;

    public basicPredictor() {
        basicPredictor((1 << 3));
    }

    public basicPredictor(int x) {
        //x = 1 << Bits = 2^Bits
        max = x - 1;
        min = 0;
        flip = x / 2;
        current = flip;
    }

    public boolean predict() {
        return current >= flip;
    }

    public void update(boolean actualResult) {
        if (actualResult) {
            if (current < max) {
                current++;
            }
        } else {
            if (current > min) {
                current--;
            }
        }
    }
}

class smithPredictor {
    basicPredictor BP;

    public smithPredictor(int x) {
        BP = new basicPredictor(x);
    }

    public boolean call() {
        return BP.predict();
    }

    public void update(boolean actualResult) {
        BP.update(actualResult);
    }

    public String print() {
        return Integer.toString(BP.current);
    }
}

class bimodalPredictor {
    gsharePredictor GP;

    public bimodalPredictor() {
        //GP = new gsharePredictor(m, 0, tracefile);
    }

    public boolean call() {
        //return GP.call();
    }

    public String print() {
        //return GP.print();
    }
}

class gsharePredictor {
    basicPredictor[] BPArray;
    int mMask, GBHR, nMask, index;

    public gsharePredictor() {
        //index = 0
        //mMask = 2^m - 1
        //nBits = n // TODO Double Check
        //GBHR = 0
        //BPArray = new basicPredictor[2^m]
            //parallelized loop to init each?
    }

    public boolean call() {
        //index = ((PC>>2) & mMask) ^ GBHR
        //return BPArray[index].predict();
    }

    public void updateBP() {
        //!ASSUMES compiler doesn't switch operating order, uses index calculated by prior call()
        //BPArray[index].update(actualResult);
    }

    public void updateGBHR() {
        //if nBits == 0 return
        //GBHR = GBHR >> 1
        //if (actualResult)
            //GBHR = (1 << n) | GBHR
    }

    public String print() {
        //retString
        //for (BPArray.length)
            //retString = "\n" + i + "\t" + BPArray[i].current
        //return retString
    }
}

class hybridPredictor {
    basicPredictor[] chooser;
    bimodalPredictor bmPredictor;
    gsharePredictor gsPredictor;
    int kMask;

    public hybridPredictor() {
        //bmPredictor = new bimodalPredictor()
        //gsPredictor = new gsharePredictor()
        //kMask = 2^k - 1
            //TODO we don't need Math.pow(), we can use left shift operations since we want powers of 2
        //BPArray = new basicPredictor[2^k]
            //parallelized loop to init each? basicPredictor(2)
    }

    public boolean predict() {
        //chooseIndex = (pc >> 2) & kMask
        //retG = gsPredictor.call()
        //retB = bmPredictor.call()
        //if (chooser[chooseIndex].call())
            //ret = retG
            //gsPredictor.updateBP()
        //else
            //ret = retB
            //bmPredictor.updateBP()
        //gsPredictor.updateGBHR()
        //if retB == retG {break;}
        //else if retG == actualResult {chooser[chooseIndex].update(t)}
        //else {chooser[chooseIndex].update(n)}
        //return ret
    }

    public String print() {
        //retString
        //for (chooser.length)
            //retString = "\n" + i + "\t" + chooser[i].current
    }
}