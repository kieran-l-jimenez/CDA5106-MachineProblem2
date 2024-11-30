import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.OutputStreamWriter;

class sim {
    public static void main(String[] args) {
        try {
            BufferedReader fileReader;
            BufferedWriter myWriter = new BufferedWriter(new OutputStreamWriter(System.out));
            String tracefileString, lineString;
            int accesses, mispredictions;

            switch (args[0]) {
                case "smith":
                    // initialize values
                    int B = Integer.parseInt(args[1]);
                    tracefileString = args[2];

                    // validate inputs
                    //if (B <= 0 || tracefileString == null);

                    // open file
                    fileReader = new BufferedReader(new FileReader(tracefileString));

                    // initialize accesses and mispredictions
                    accesses = 0;
                    mispredictions = 0;
                    smithPredictor SP = new smithPredictor(1 << B);

                    // iterate through trace
                    while ((lineString = fileReader.readLine()) != null) {
                        // split line
                        String[] components = lineString.split(" ");
                        boolean actualResult = components[1].equals("t");

                        // get result from predictor
                        boolean predictedResult = SP.call();
                        SP.update(actualResult);

                        accesses++;
                        if (predictedResult != actualResult)
                            mispredictions++;
                    }

                    myWriter.write("COMMAND\n");
                    myWriter.write("./sim smith " + B + " " + tracefileString + "\n");
                    myWriter.write("OUTPUT\n");
                    myWriter.write("number of predictions:\t" + accesses + "\n");
                    myWriter.write("number of mispredictions:\t" + mispredictions + "\n");
                    myWriter.write("misprediction rate:\t" + String.format("%.2f", (double) (100.0 * mispredictions) / accesses) + "%\n");
                    myWriter.write("FINAL COUNTER CONTENT:\t");
                    myWriter.write(SP.print());
                    myWriter.close();
                    break;

                case "bimodal":
                    // initialize values
                    int M2 = Integer.parseInt(args[1]);
                    tracefileString = args[2];
                    fileReader = new BufferedReader(new FileReader(tracefileString));
                    accesses = 0;
                    mispredictions = 0;
                    bimodalPredictor BP = new bimodalPredictor(1 << M2);
                    // initialize accesses and mispredictions
                    while ((lineString = fileReader.readLine()) != null) {
                        // split line
                        String[] components = lineString.split(" ");
                        boolean actualResult = components[1].equals("t");
                        // convert hex to decimal
                        int PC = Integer.parseInt(components[0],16);

                        // get result from predictor
                        boolean predictedResult = BP.call(PC);
                        BP.updateBP(actualResult);

                        accesses++;
                        if (predictedResult != actualResult)
                            mispredictions++;
                    }

                    myWriter.write("COMMAND\n");
                    myWriter.write("./sim bimodal " + M2 + " " + tracefileString + "\n");
                    myWriter.write("OUTPUT\n");
                    myWriter.write("number of predictions:\t" + accesses + "\n");
                    myWriter.write("number of mispredictions:\t" + mispredictions + "\n");
                    myWriter.write("misprediction rate:\t" + String.format("%.2f", (double) (100.0 * mispredictions) / accesses) + "%\n");
                    myWriter.write("FINAL BIMODAL CONTENTS");
                    myWriter.write(BP.print());
                    myWriter.close();
                    break;

                case "gshare":
                    // initialize values
                    int M1 = Integer.parseInt(args[1]);
                    int N = Integer.parseInt(args[2]);
                    tracefileString = args[3];
                    fileReader = new BufferedReader(new FileReader(tracefileString));
                    accesses = 0;
                    mispredictions = 0;
                    gsharePredictor GP = new gsharePredictor(1 << M1, N);
                    // initialize accesses and mispredictions
                    while ((lineString = fileReader.readLine()) != null) {
                        // split line
                        String[] components = lineString.split(" ");
                        boolean actualResult = components[1].equals("t");
                        // convert hex to decimal
                        int PC = Integer.parseInt(components[0],16);

                        // get result from predictor
                        boolean predictedResult = GP.call(PC);
                        GP.updateBP(actualResult);
                        GP.updateGBHR(actualResult);

                        accesses++;
                        if (predictedResult != actualResult)
                            mispredictions++;
                    }

                    myWriter.write("COMMAND\n");
                    myWriter.write("./sim gshare " + M1 + " " + N + " " + tracefileString + "\n");
                    myWriter.write("OUTPUT\n");
                    myWriter.write("number of predictions:\t" + accesses + "\n");
                    myWriter.write("number of mispredictions:\t" + mispredictions + "\n");
                    myWriter.write("misprediction rate:\t" + String.format("%.2f", (double) (100.0 * mispredictions) / accesses) + "%\n");
                    myWriter.write("FINAL GSHARE CONTENTS");
                    myWriter.write(GP.print());
                    myWriter.close();
                    break;

                case "hybrid":
                    // initialize values
                    int K = Integer.parseInt(args[1]);
                    int hM1 = Integer.parseInt(args[2]);
                    int hN = Integer.parseInt(args[3]);
                    int hM2 = Integer.parseInt(args[4]);
                    tracefileString = args[5];

                    fileReader = new BufferedReader(new FileReader(tracefileString));
                    accesses = 0;
                    mispredictions = 0;
                    hybridPredictor HP = new hybridPredictor(1 << K, hM1, hN, hM2);
                    // initialize accesses and mispredictions
                    while ((lineString = fileReader.readLine()) != null) {
                        // split line
                        String[] components = lineString.split(" ");
                        boolean actualResult = components[1].equals("t");
                        // convert hex to decimal
                        int PC = Integer.parseInt(components[0],16);

                        // get result from predictor
                        boolean predictedResult = HP.predict(PC, actualResult);

                        accesses++;
                        if (predictedResult != actualResult)
                            mispredictions++;
                    }

                    myWriter.write("COMMAND\n");
                    myWriter.write("./sim hybrid " + K + " " + hM1 + " " + hN + " " + hM2 + " " + tracefileString + "\n");
                    myWriter.write("OUTPUT\n");
                    myWriter.write("number of predictions:\t" + accesses + "\n");
                    myWriter.write("number of mispredictions:\t" + mispredictions + "\n");
                    myWriter.write("misprediction rate:\t" + String.format("%.2f", (double) (100.0 * mispredictions) / accesses) + "%\n");
                    myWriter.write("FINAL CHOOSER CONTENTS");
                    myWriter.write(HP.print());
                    myWriter.close();
                    break;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class basicPredictor {
    public int max, min, current, flip;

    public basicPredictor() {
        this((1 << 3));
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

    public bimodalPredictor(int m) {
        GP = new gsharePredictor(m, 0);
    }

    public boolean call(int PC) {
        return GP.call(PC);
    }

    public void updateBP(boolean actualResult) {
        GP.updateBP(actualResult);
    }

    public String print() {
        return GP.print();
    }
}

class gsharePredictor {
    basicPredictor[] predictionTable;
    int index, mMask, nBits, GBHR;

    public gsharePredictor(int m, int n) {
        //m = 1 << mBits = 2^mBits
        index = 0;
        mMask = m - 1;
        nBits = n;
        GBHR = 0;
        predictionTable = new basicPredictor[m];
        //TODO parallelize this loop if takes more than 2 minutes
        for (int i = 0; i < predictionTable.length; i++) {
            predictionTable[i] = new basicPredictor();
        }
    }

    public boolean call(int PC) {
        //1. PC >>> 2    : discard 2 bottom bits of PC
        //2. & mMask    : get m low-order bits (aka, bits m+1 through 2)
        //3. ^ GBHR     : XOR with global branch history; in bimodal there will be no change (XOR 0 does nothing)
        index = ((PC >>> 2) & mMask) ^ GBHR;
        return predictionTable[index].predict();
    }

    public void updateBP(boolean actualResult) {
        // !!! ASSUMES compiler doesn't switch operating order, uses index calculated by prior call()
        // IF there are errors, have call() pass back the index calculated along with the prediction and pass index into here
        predictionTable[index].update(actualResult);
    }

    public void updateGBHR(boolean actualResult) {
        // if there is no global history register, return
        if (nBits == 0) {
            return;
        }

        // shift GBHR bits to the right by one, fill with 0
        // (aka: default to assume branch actually not taken)
        GBHR = GBHR >>> 1;

        // if the branch was taken, place 1 in most significant bit
        if (actualResult) {
            GBHR = (1 << (nBits-1)) | GBHR;
        }
    }

    public String print() {
        //TODO convert to use StringBuffer if takes more than 2 minutes
        String retString = new String();

        //TODO parallelize this loop if takes more than 2 minutes
        for (int i = 0; i < predictionTable.length; i++) {
            retString = retString.concat("\n" + i + "\t" + predictionTable[i].current);
        }
        return retString;
    }
}

class hybridPredictor {
    basicPredictor[] chooser;
    bimodalPredictor bmPredictor;
    gsharePredictor gsPredictor;
    int kMask;

    public hybridPredictor(int k, int m1, int n, int m2) {
        // k = 1 << kBits = 2^kBits
        gsPredictor = new gsharePredictor(1 << m1, n);
        bmPredictor = new bimodalPredictor(1 << m2);
        kMask = k - 1;
        chooser = new basicPredictor[k];
        int chooserBits = 1 << 2;
        //TODO parallelize this loop if takes more than 2 minutes
        for (int i = 0; i < chooser.length; i++) {
            chooser[i] = new basicPredictor(chooserBits);
        }
    }

    public boolean predict(int PC, boolean actualResult) {
        int chooserIndex = (PC >>> 2) & kMask;
        boolean retG = gsPredictor.call(PC);
        boolean retB = bmPredictor.call(PC);
        boolean ret;

        if (chooser[chooserIndex].predict()) {
            ret = retG;
            gsPredictor.updateBP(actualResult);
        } else {
            ret = retB;
            bmPredictor.updateBP(actualResult);
        }

        gsPredictor.updateGBHR(actualResult);

        if (retB == retG) {
            //break;
        } else if (retG == actualResult) {
            chooser[chooserIndex].update(true);
        } else {
            chooser[chooserIndex].update(false);
        }

        return ret;
    }

    public String print() {
        //TODO convert to use StringBuffer if takes more than 2 minutes
        String retString = new String();

        //TODO parallelize this loop if takes more than 2 minutes
        for (int i = 0; i < chooser.length; i++) {
            retString = retString.concat("\n" + i + "\t" + chooser[i].current);
        }

        retString = retString.concat("\nFINAL GSHARE CONTENTS");
        retString = retString.concat(gsPredictor.print());
        retString = retString.concat("\nFINAL BIMODAL CONTENTS");
        retString = retString.concat(bmPredictor.print());

        return retString;
    }
}