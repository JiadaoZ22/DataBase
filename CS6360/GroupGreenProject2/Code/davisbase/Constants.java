package davisbase;

public class Constants{
  public static final int LeafPageNode = 13;
  public static final int InnerPageNode = 5;
  public static final int LeafIndexNode = 10;
  public static final int InnerIndexNode = 2;
  public static final int PageTypeOffSet = 0;
  public static final int NumRecordsOffSet = 1;
  public static final int PageContentOffSet = 2;
  public static final int RightPageOffSet = 8; //check if works
  public static final int LeftPageOffSet = 4; // check if works
  public static final int IndexFirstPointerOffSet = 12; //check if works
  public static final int TableFirstPointerOffSet = 8; //check if works
  public static final int PayloadOffSet = 2;
  public static final int RowIdOffSet = 4;
  public static final int DataTypeOffSet = 1;
  public static final int PayloadSize = 2;
  public static final int tableRightPageOffSet = 4;

  public static final int TinyIntTypeCode = 1;
  public static final int SmallIntTypeCode = 2;
  public static final int IntTypeCode = 3;
  public static final int BigIntTypeCode = 4;
  public static final int FloatTypeCode = 5;
  public static final int DoubleTypeCode = 6;
  public static final int DateTimeTypeCode = 7;
  public static final int DateTypeCode = 8;

  public static final int TinyIntSize = 1;
  public static final int SmallIntSize = 2;
  public static final int IntSize = 4;
  public static final int BigIntSize = 8;
  public static final int FloatSize = 4;
  public static final int DoubleSize = 8;
  public static final int DateTimeSize = 8;
  public static final int DateTypeSize = 8;
}
