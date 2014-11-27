/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;
import org.json.JSONArray;

/**
 *
 * @author Coryn Scott
 */
public class testcase {
    
    public static void main(String[] args){
        String[] numbers = new String[3];
        numbers[0]="+447709829402";
        numbers[1]="07709829403";
        //Doesnt exist on the server
        numbers[2]="+447709829409"; 
        JSONArray numbersJson = new JSONArray(numbers);
        String data = numbersJson.toString();
        System.out.println(data);
    }
}
