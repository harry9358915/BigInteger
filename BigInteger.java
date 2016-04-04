//U10316050 ²ø´¼µ¾
import java.util.*;
import java.lang.*;
public class BigInteger {
    private List<Integer> value;
    
    public BigInteger(String val) {
        // get number
        String v = val.charAt(0) == '-' ? val.substring(1) : val;
        // four bits return one int
        value = new ArrayList<>();
        for(int i = v.length() - 4; i > -4; i -= 4) {
            value.add(Integer.parseInt(v.substring(i >= 0 ? i : 0, i + 4)));
        }// fill up
        int valueLength = (value.size() / 8 + 1) * 8;
        for(int i = value.size(); i < valueLength; i++) {
            value.add(0);
        }
        // Complement
        value = val.charAt(0) == '-' ? toComplement(value) : value;        
    }

    private BigInteger(List<Integer> value) {
        this.value = value;
    }
    
    public BigInteger add(BigInteger that) {
        if(isNegative(that.value)) {
            return subtract(new BigInteger(toComplement(that.value)));
        }
        //align
        int length = Math.max(value.size(), that.value.size());
        List<Integer> op1 = copyOf(value, length);
        List<Integer> op2 = copyOf(that.value, length);
        List<Integer> result = new ArrayList<>();
        
        int carry = 0;
        for(int i = 0; i < length - 1; i++) {
            int c = op1.get(i) + op2.get(i) + carry;
            if(c < 10000) {
                carry = 0;
            } else {
                c -= 10000;
                carry = 1;
            }
            result.add(c);
        }
        
        if(carry == 1) { // Overflow
            if(isPositive(op1)) 
            	{ result.add(1); } 
            else
            	{ result.clear(); }  
            for(int i = 0; i < 8; i++) 
            	{ result.add(0); } 
        	} 
        else {result.add(isPositive(op1) ? 0 : 9999);
        }
        return new BigInteger(result);
    }
    
    public BigInteger subtract(BigInteger that) {
        if(isNegative(that.value)) {
            return add(new BigInteger(toComplement(that.value)));
        }
        //align
        int length = Math.max(value.size(), that.value.size());
        List<Integer> op1 = copyOf(value, length);
        List<Integer> op2 = copyOf(that.value, length);
        List<Integer> result = new ArrayList<>();
        
        int borrow = 0;
        for(int i = 0; i < length - 1; i++) { 
            int c = op1.get(i) - op2.get(i) - borrow; 
            if(c > -1) {
                borrow = 0;
            } else { 
                c += 10000; 
                borrow = 1; 
            }
            result.add(c);
        }
        
        if(borrow == 1) { 
            if(isNegative(op1)) { result.add(9998); } 
            else { result.clear(); } 
            for(int i = 0; i < 8; i++) { result.add(9999); } 
        } else {  
            result.add(isNegative(op1) ? 9999 : 0);
        }
        
        return new BigInteger(result);
    }

   
    public String toString() {
        List<Integer> v = isNegative(value) ? toComplement(value) : value;
        StringBuilder builder = new StringBuilder();
        for(int i = v.size() - 1; i > -1; i--) {
            builder.append(String.format("%04d", v.get(i)));
        }
        while(builder.length() > 0 && builder.charAt(0) == '0') {
            builder.deleteCharAt(0);
        }
        return builder.length() == 0 ? "0" : 
                   isNegative(value) ? builder.insert(0, '-').toString() : 
                       builder.toString();
    }
        
    private static List<Integer> toComplement(List<Integer> v) {
        List<Integer> comp = new ArrayList<>();
        for(Integer i : v) { comp.add(9999 - i); }
        comp.set(0, comp.get(0) + 1);
        return comp;
    }
    
    private static List<Integer> copyOf(List<Integer> original, int newLength) {
        List<Integer> v = new ArrayList<>(original);
        for(int i = v.size(); i < newLength; i++) {
            v.add(isPositive(original) ? 0 : 9999);
        }
        return v;
    }
    
    private static Integer getLast(List<Integer> list) {
        return list.get(list.size() - 1);
    }
    
    private static boolean isNegative(List<Integer> list) {
        return getLast(list) == 9999;
    }
    
    private static boolean isPositive(List<Integer> list) {
        return getLast(list) == 0;
    }
    
    public static void main(String[] args) {
        BigInteger a = new BigInteger("-999999999999999999999999992");
        BigInteger b = new BigInteger("-1");
        System.out.println(a.add(b));       
        System.out.println(a.subtract(b)); 
    }
}
