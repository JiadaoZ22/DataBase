package davisbase;
import java.util.*;

public class Utils{
  public Utils(){

  }

  public static byte [] convertByteArrayListToByteArray(ArrayList<Byte> byteArrayList){
    byte [] byteArray = new byte[byteArrayList.size()];
    Iterator<Byte> iterator = byteArrayList.iterator();
    int index = 0;
    while(iterator.hasNext()){
      byteArray[index] = iterator.next().byteValue();
      index++;
    }
    return byteArray;
  }

  public static byte [] unboxByteArray(Byte byteObjects []){
    byte [] bytes = new byte[byteObjects.length];
    int i = 0;
    for(Byte b: byteObjects)
      bytes[i++] = b;
    return bytes;
  }

  public static Byte [] boxByteArray(byte bytes[]){
    Byte [] byteObjects = new Byte[bytes.length];
    int i = 0;
    for(byte b : bytes)
      byteObjects[i++] = b;
    return byteObjects;
  }

}
