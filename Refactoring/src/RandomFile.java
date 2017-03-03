import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

class RandomFile {
    private RandomAccessFile output;
    private RandomAccessFile input;


    public void createFile(String fileName) {
        RandomAccessFile file = null;

        try {
            file = new RandomAccessFile(fileName, "rw");

        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Error processing file!");
            System.exit(1);
        }

    }

    public RandomAccessFile openFile(String s, RandomAccessFile file, String permission) {

        try {
            file = new RandomAccessFile(s, permission);
            return file;

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File does not exist!");
        }
        return null;
    }



    public void openingWriteFile(String fileName) {
        output = openFile(fileName, output, "rw");
    }

    public void openingReadFile(String fileName) {
        input = openFile(fileName, input, "r");
    }

    public void close(RandomAccessFile Close){


        try
        {
            if (Close != null)
                Close.close();
        }
        catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Error closing file!");
            ioException.printStackTrace();
        }
    }

    public void closeWriteFile(){
        close(output);
    }

    public void closeReadFile(){
        close(input);
    }

    public long addRecords(Employee employeeToAdd) {
        long currentRecordStart = 0;

        RandomAccessEmployeeRecord record;

        try
        {
            record = new RandomAccessEmployeeRecord(employeeToAdd.getEmployeeId(), employeeToAdd.getPps(),
                    employeeToAdd.getSurname(), employeeToAdd.getFirstName(), employeeToAdd.getGender(),
                    employeeToAdd.getDepartment(), employeeToAdd.getSalary(), employeeToAdd.getFullTime());

            output.seek(output.length());
            record.write(output);
            currentRecordStart = output.length();
        }
        catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Error writing to file!");
        }

        return currentRecordStart - RandomAccessEmployeeRecord.SIZE;
    }

    public void changeRecords(Employee newDetails, long byteToStart) {
        RandomAccessEmployeeRecord record;
        try
        {
            record = new RandomAccessEmployeeRecord(newDetails.getEmployeeId(), newDetails.getPps(),
                    newDetails.getSurname(), newDetails.getFirstName(), newDetails.getGender(),
                    newDetails.getDepartment(), newDetails.getSalary(), newDetails.getFullTime());

            output.seek(byteToStart);
            record.write(output);
        }
        catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Error writing to file!");
        }
    }

    public void deleteRecords(long byteToStart) {

        RandomAccessEmployeeRecord record;


        try
        {
            record = new RandomAccessEmployeeRecord();
            output.seek(byteToStart);
            record.write(output);
        }
        catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Error writing to file!");
        }
    }

    public void openReadFile(String fileName) {
        try
        {
            input = new RandomAccessFile(fileName, "r");
        }
        catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "File is not supported!");
        }
    }



    public long getFirst() {
        long byteToStart = 0;

        try {
            input.length();
        }
        catch (IOException ignored) {
        }

        return byteToStart;
    }

    public long getLast() {
        long byteToStart = 0;

        try {
            byteToStart = input.length() - RandomAccessEmployeeRecord.SIZE;
        }
        catch (IOException ignored) {
        }

        return byteToStart;
    }

    public long getNext(long readFrom) {
        long byteToStart = readFrom;

        try {
            input.seek(byteToStart);
            if (byteToStart + RandomAccessEmployeeRecord.SIZE == input.length())
                byteToStart = 0;
            else
                byteToStart = byteToStart + RandomAccessEmployeeRecord.SIZE;
        }
        catch (NumberFormatException ignored) {
        }
        catch (IOException ignored) {
        }return byteToStart;
    }


    public long getPrevious(long readFrom) {
        long byteToStart = readFrom;

        try {
            input.seek(byteToStart);
            if (byteToStart == 0)
                byteToStart = input.length() - RandomAccessEmployeeRecord.SIZE;
            else
                byteToStart = byteToStart - RandomAccessEmployeeRecord.SIZE;
        }
        catch (NumberFormatException ignored) {
        }
        catch (IOException ignored) {
        }
        return byteToStart;
    }

    public Employee readRecords(long byteToStart) {
        Employee thisEmp;
        RandomAccessEmployeeRecord record = new RandomAccessEmployeeRecord();

        try {
            input.seek(byteToStart);
            record.read(input);
        }
        catch (IOException ignored) {
        }

        thisEmp = record;

        return thisEmp;
    }

    public boolean isPpsExist(String pps, long currentByteStart) {
        RandomAccessEmployeeRecord record = new RandomAccessEmployeeRecord();
        boolean ppsExist = false;
        long currentByte = 0;

        try {
            while (currentByte != input.length() && !ppsExist) {
                if (currentByte != currentByteStart) {
                    input.seek(currentByte);
                    record.read(input);
                    if (record.getPps().trim().equalsIgnoreCase(pps)) {
                        ppsExist = true;
                        JOptionPane.showMessageDialog(null, "PPS number already exist!");
                    }
                }
                currentByte = currentByte + RandomAccessEmployeeRecord.SIZE;
            }
        }
        catch (IOException ignored) {
        }

        return ppsExist;
    }

    public boolean isSomeoneToDisplay() {
        boolean someoneToDisplay = false;
        long currentByte = 0;
        RandomAccessEmployeeRecord record = new RandomAccessEmployeeRecord();

        try {
            while (currentByte != input.length() && !someoneToDisplay) {
                input.seek(currentByte);
                record.read(input);
                if (record.getEmployeeId() > 0)
                    someoneToDisplay = true;
                currentByte = currentByte + RandomAccessEmployeeRecord.SIZE;
            }
        }
        catch (IOException ignored) {
        }

        return someoneToDisplay;
    }
}
