/* The following code was generated by JFlex 1.4.1 on 16/08/16 22:47 */

/*
 * Generated on 8/16/16 10:47 PM
 */
package xyz.algogo.desktop.res.lang.highlighting;

import java.io.*;
import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;


/**
 * 
 */

public class fr extends AbstractJFlexCTokenMaker {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\14\1\7\1\0\1\14\1\6\22\0\1\14\1\30\1\6"+
    "\1\0\1\1\1\31\1\51\1\6\2\27\1\31\1\32\1\6\1\20"+
    "\1\16\1\31\1\4\3\3\4\3\2\3\1\30\1\6\1\52\1\50"+
    "\1\53\1\30\1\0\1\24\1\13\1\5\1\36\1\17\1\23\1\42"+
    "\1\33\1\35\2\1\1\25\1\41\1\12\1\45\1\34\1\47\1\22"+
    "\1\26\1\21\1\11\1\40\1\1\1\15\1\46\1\1\1\27\1\10"+
    "\1\27\1\30\1\2\1\0\1\24\1\13\1\5\1\36\1\17\1\23"+
    "\1\42\1\33\1\35\2\1\1\25\1\41\1\12\1\45\1\34\1\47"+
    "\1\22\1\26\1\21\1\11\1\40\1\1\1\15\1\46\1\1\1\27"+
    "\1\54\1\27\1\30\101\0\1\43\10\0\1\37\4\0\1\44\21\0"+
    "\1\43\10\0\1\37\4\0\1\44\uff11\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\2\1\2\2\1\1\1\3\1\1\1\4\1\1"+
    "\1\5\2\1\1\6\2\1\1\7\3\5\4\1\1\6"+
    "\4\5\1\0\1\10\1\0\2\10\3\1\1\11\11\1"+
    "\1\6\1\1\1\0\2\1\1\0\1\11\1\0\1\12"+
    "\1\1\1\0\6\1\1\13\6\1\1\0\2\1\1\0"+
    "\1\1\1\0\1\1\1\0\1\1\1\14\1\1\1\15"+
    "\5\1\1\6\1\0\2\1\3\0\5\1\1\13\2\1"+
    "\2\0\1\15\6\1\1\0\6\1\1\0\4\1\1\0"+
    "\3\1\1\0\2\1\1\0\2\1\1\0\5\1";

  private static int [] zzUnpackAction() {
    int [] result = new int[140];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\55\0\132\0\207\0\264\0\341\0\55\0\u010e"+
    "\0\u013b\0\u0168\0\u0195\0\u01c2\0\u01ef\0\u021c\0\u0249\0\u0276"+
    "\0\55\0\55\0\u02a3\0\u02d0\0\u02fd\0\u032a\0\u0357\0\u0384"+
    "\0\55\0\u03b1\0\u03de\0\u040b\0\u0438\0\u0465\0\u0492\0\u0168"+
    "\0\u04bf\0\u04ec\0\u0519\0\u0546\0\u0573\0\u05a0\0\u05cd\0\u05fa"+
    "\0\u0627\0\u0654\0\u0681\0\u06ae\0\u06db\0\u0708\0\u0735\0\u0762"+
    "\0\u078f\0\u07bc\0\u07e9\0\u0816\0\u0843\0\u0870\0\u089d\0\u04ec"+
    "\0\u08ca\0\u08f7\0\u0924\0\u0951\0\u097e\0\u09ab\0\u09d8\0\u0a05"+
    "\0\132\0\u0a32\0\u0a5f\0\u0a8c\0\u0ab9\0\u0ae6\0\u0b13\0\u0b40"+
    "\0\u0b6d\0\u0b9a\0\u0bc7\0\u0bf4\0\u0c21\0\u0c4e\0\u0c7b\0\u0ca8"+
    "\0\132\0\u0cd5\0\132\0\u0d02\0\u0d2f\0\u0d5c\0\u0d89\0\u0db6"+
    "\0\132\0\u0de3\0\u0e10\0\u0e3d\0\u0e6a\0\u0e97\0\u0ec4\0\u0ef1"+
    "\0\u0f1e\0\u0f4b\0\u0f78\0\u0fa5\0\55\0\u0fd2\0\u0fff\0\u102c"+
    "\0\u1059\0\55\0\u1086\0\u10b3\0\u10e0\0\u110d\0\u113a\0\u1167"+
    "\0\u1194\0\u11c1\0\u11ee\0\u121b\0\u1248\0\u1275\0\u12a2\0\u12cf"+
    "\0\u12fc\0\u1329\0\u1356\0\u1383\0\u13b0\0\u13dd\0\u140a\0\u1437"+
    "\0\u1464\0\u1491\0\u14be\0\u14eb\0\u1518\0\u1545\0\u1572\0\u159f"+
    "\0\u15cc\0\u15f9\0\u1626\0\u1653";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[140];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\2\3\1\4\1\5\1\6\1\2\1\7\1\2"+
    "\1\3\1\10\1\3\1\11\1\3\1\12\1\3\1\13"+
    "\1\14\1\3\1\15\1\16\1\17\1\20\1\21\1\22"+
    "\1\23\1\24\1\3\1\25\1\3\1\26\1\2\1\27"+
    "\1\30\1\3\1\31\1\2\3\3\1\23\1\32\1\33"+
    "\1\34\1\35\56\0\5\3\2\0\1\36\3\3\1\0"+
    "\1\3\1\0\1\3\1\0\6\3\4\0\4\3\1\0"+
    "\3\3\2\0\3\3\5\0\3\37\2\4\1\37\2\0"+
    "\4\37\1\0\1\37\1\40\1\41\1\0\6\37\4\0"+
    "\15\37\5\0\3\37\2\4\1\37\2\0\4\37\1\0"+
    "\1\42\1\40\1\41\1\0\6\37\4\0\15\37\6\0"+
    "\5\3\2\0\1\36\3\3\1\0\1\3\1\0\1\3"+
    "\1\0\1\3\1\43\4\3\4\0\1\44\3\3\1\0"+
    "\3\3\2\0\3\3\6\0\5\3\2\0\1\36\3\3"+
    "\1\0\1\3\1\0\1\3\1\0\6\3\4\0\4\3"+
    "\1\0\3\3\2\0\1\45\2\3\21\0\1\11\43\0"+
    "\2\46\70\0\1\22\27\0\1\22\5\0\5\3\2\0"+
    "\1\36\3\3\1\0\1\3\1\0\1\3\1\0\1\3"+
    "\1\47\1\3\1\50\2\3\4\0\4\3\1\0\3\3"+
    "\2\0\1\3\1\51\1\3\6\0\5\3\2\0\1\36"+
    "\3\3\1\0\1\3\1\0\1\3\1\0\3\3\1\52"+
    "\2\3\4\0\2\3\1\53\1\3\1\0\3\3\2\0"+
    "\3\3\6\0\5\3\2\0\1\36\3\3\1\0\1\3"+
    "\1\0\1\3\1\0\2\3\1\54\1\3\1\55\1\56"+
    "\4\0\4\3\1\0\3\3\2\0\3\3\6\0\5\3"+
    "\2\0\1\36\3\3\1\0\1\3\1\0\1\3\1\0"+
    "\6\3\4\0\2\3\1\57\1\3\1\0\3\3\2\0"+
    "\3\3\6\0\5\3\2\0\1\36\3\3\1\0\1\3"+
    "\1\0\1\3\1\0\6\3\4\0\2\3\1\60\1\3"+
    "\1\0\3\3\2\0\3\3\55\0\1\22\36\0\1\22"+
    "\15\0\1\22\5\0\5\3\2\0\1\36\3\3\1\0"+
    "\1\3\1\0\1\3\1\0\6\3\4\0\4\3\1\0"+
    "\3\3\2\0\1\61\2\3\6\0\5\3\2\0\1\36"+
    "\3\3\1\0\1\3\1\0\1\3\1\0\6\3\4\0"+
    "\4\3\1\62\3\3\2\0\3\3\6\0\5\3\2\0"+
    "\1\36\3\3\1\0\1\3\1\0\1\3\1\0\3\3"+
    "\1\63\2\3\4\0\4\3\1\0\3\3\2\0\3\3"+
    "\6\0\5\3\2\0\1\36\3\3\1\0\1\3\1\0"+
    "\1\64\1\0\6\3\4\0\4\3\1\0\3\3\2\0"+
    "\3\3\56\0\1\22\55\0\1\23\55\0\1\23\55\0"+
    "\1\22\11\0\1\65\43\0\6\37\2\0\4\37\1\0"+
    "\1\37\1\0\1\37\1\0\6\37\4\0\15\37\5\0"+
    "\3\37\2\66\1\37\2\0\4\37\1\0\1\37\1\0"+
    "\1\37\1\67\6\37\3\0\1\67\15\37\5\0\3\37"+
    "\3\70\2\0\3\37\1\70\1\0\1\37\1\0\1\70"+
    "\1\0\2\37\2\70\2\37\4\0\3\37\1\70\11\37"+
    "\6\0\5\3\2\0\1\36\3\3\1\0\1\3\1\0"+
    "\1\71\1\0\6\3\4\0\4\3\1\72\3\3\2\0"+
    "\3\3\6\0\5\3\2\0\1\36\3\3\1\0\1\3"+
    "\1\0\1\3\1\0\3\3\1\73\2\3\4\0\4\3"+
    "\1\0\3\3\2\0\3\3\6\0\5\3\2\0\1\36"+
    "\3\3\1\0\1\3\1\0\1\3\1\0\6\3\4\0"+
    "\4\3\1\0\1\3\1\74\1\3\2\0\3\3\5\0"+
    "\3\37\2\46\1\37\2\0\4\37\1\0\1\37\1\0"+
    "\1\41\1\0\6\37\4\0\15\37\6\0\5\3\2\0"+
    "\1\36\1\75\2\3\1\0\1\3\1\0\1\3\1\0"+
    "\6\3\4\0\4\3\1\0\3\3\2\0\3\3\6\0"+
    "\5\3\2\0\1\36\1\3\1\76\1\3\1\0\1\3"+
    "\1\0\1\3\1\0\6\3\4\0\4\3\1\0\3\3"+
    "\2\0\3\3\6\0\5\3\2\0\1\36\3\3\1\0"+
    "\1\3\1\0\1\3\1\0\6\3\4\0\1\3\1\77"+
    "\2\3\1\0\3\3\2\0\3\3\6\0\5\3\2\0"+
    "\1\36\3\3\1\0\1\3\1\0\1\3\1\0\4\3"+
    "\1\100\1\3\4\0\4\3\1\0\3\3\2\0\3\3"+
    "\6\0\5\3\2\0\1\36\1\3\1\101\1\3\1\0"+
    "\1\3\1\0\1\3\1\0\6\3\4\0\4\3\1\0"+
    "\3\3\2\0\3\3\6\0\5\3\2\0\1\36\3\3"+
    "\1\0\1\3\1\0\1\3\1\0\2\3\1\102\3\3"+
    "\4\0\4\3\1\0\3\3\2\0\3\3\6\0\5\3"+
    "\2\0\1\36\3\3\1\0\1\3\1\0\1\3\1\0"+
    "\4\3\1\103\1\3\4\0\4\3\1\0\3\3\2\0"+
    "\3\3\6\0\5\3\2\0\1\36\3\3\1\0\1\3"+
    "\1\0\1\3\1\0\5\3\1\104\4\0\4\3\1\0"+
    "\3\3\2\0\3\3\6\0\5\3\2\0\1\36\3\3"+
    "\1\0\1\3\1\0\1\3\1\0\1\3\1\105\4\3"+
    "\4\0\4\3\1\0\3\3\2\0\3\3\6\0\5\3"+
    "\2\0\1\36\1\3\1\106\1\3\1\0\1\3\1\0"+
    "\1\3\1\0\6\3\4\0\4\3\1\0\3\3\2\0"+
    "\3\3\6\0\5\3\2\0\1\36\1\107\2\3\1\0"+
    "\1\3\1\0\1\3\1\0\6\3\4\0\4\3\1\0"+
    "\3\3\2\0\3\3\20\0\1\110\42\0\5\3\2\0"+
    "\1\36\3\3\1\0\1\3\1\0\1\3\1\0\1\3"+
    "\1\111\4\3\4\0\4\3\1\0\3\3\2\0\3\3"+
    "\6\0\5\3\2\0\1\36\3\3\1\0\1\3\1\0"+
    "\1\3\1\0\5\3\1\112\4\0\4\3\1\0\3\3"+
    "\2\0\3\3\10\0\3\113\5\0\1\113\3\0\1\113"+
    "\3\0\2\113\11\0\1\113\16\0\3\37\2\66\1\37"+
    "\2\0\4\37\1\0\1\37\1\0\1\37\1\0\6\37"+
    "\4\0\15\37\10\0\2\66\51\0\5\3\2\0\1\36"+
    "\3\3\1\0\1\3\1\0\1\114\1\0\6\3\4\0"+
    "\4\3\1\0\3\3\2\0\3\3\24\0\1\115\36\0"+
    "\5\3\2\0\1\36\3\3\1\0\1\3\1\0\1\3"+
    "\1\0\6\3\4\0\2\3\1\116\1\3\1\0\3\3"+
    "\1\0\1\117\3\3\6\0\5\3\2\0\1\36\2\3"+
    "\1\120\1\0\1\3\1\0\1\3\1\0\6\3\4\0"+
    "\4\3\1\0\3\3\2\0\3\3\6\0\5\3\2\0"+
    "\1\36\3\3\1\0\1\3\1\0\1\121\1\0\6\3"+
    "\4\0\4\3\1\0\3\3\2\0\3\3\6\0\5\3"+
    "\2\0\1\36\3\3\1\0\1\3\1\0\1\3\1\0"+
    "\1\122\5\3\4\0\4\3\1\0\3\3\2\0\3\3"+
    "\6\0\5\3\2\0\1\36\3\3\1\0\1\3\1\0"+
    "\1\123\1\0\6\3\4\0\4\3\1\0\3\3\2\0"+
    "\3\3\6\0\5\3\2\0\1\36\3\3\1\0\1\3"+
    "\1\0\1\3\1\0\5\3\1\75\4\0\4\3\1\0"+
    "\3\3\2\0\3\3\6\0\5\3\2\0\1\36\3\3"+
    "\1\0\1\3\1\0\1\3\1\0\6\3\4\0\2\3"+
    "\1\124\1\3\1\0\3\3\2\0\3\3\6\0\5\3"+
    "\2\0\1\36\3\3\1\0\1\3\1\0\1\3\1\0"+
    "\3\3\1\125\2\3\4\0\4\3\1\0\3\3\2\0"+
    "\3\3\6\0\5\3\2\0\1\36\3\3\1\0\1\3"+
    "\1\0\1\3\1\0\6\3\4\0\2\3\1\126\1\3"+
    "\1\0\3\3\2\0\3\3\6\0\5\3\2\0\1\36"+
    "\3\3\1\0\1\3\1\0\1\127\1\0\6\3\4\0"+
    "\4\3\1\0\3\3\2\0\3\3\6\0\5\3\2\0"+
    "\1\36\3\3\1\0\1\3\1\0\1\3\1\0\6\3"+
    "\4\0\4\3\1\0\3\3\2\0\1\130\2\3\6\0"+
    "\5\3\2\0\1\36\3\3\1\0\1\3\1\0\1\3"+
    "\1\0\1\3\1\131\4\3\4\0\4\3\1\0\3\3"+
    "\2\0\3\3\16\0\1\132\44\0\5\3\2\0\1\36"+
    "\3\3\1\0\1\3\1\0\1\3\1\0\6\3\4\0"+
    "\2\3\1\133\1\3\1\0\3\3\2\0\3\3\6\0"+
    "\5\3\2\0\1\36\3\3\1\0\1\3\1\0\1\3"+
    "\1\0\5\3\1\134\4\0\4\3\1\0\3\3\2\0"+
    "\3\3\10\0\3\135\5\0\1\135\3\0\1\135\3\0"+
    "\2\135\11\0\1\135\17\0\5\3\2\0\1\36\3\3"+
    "\1\0\1\3\1\0\1\3\1\0\1\3\1\127\4\3"+
    "\4\0\4\3\1\0\3\3\2\0\3\3\27\0\1\136"+
    "\33\0\5\3\2\0\1\36\1\3\1\77\1\3\1\0"+
    "\1\3\1\0\1\3\1\0\6\3\4\0\4\3\1\0"+
    "\3\3\2\0\3\3\17\0\1\137\43\0\5\3\2\0"+
    "\1\36\3\3\1\0\1\3\1\0\1\3\1\0\1\3"+
    "\1\77\4\3\4\0\4\3\1\0\3\3\2\0\3\3"+
    "\6\0\1\3\1\140\3\3\2\0\1\36\3\3\1\0"+
    "\1\3\1\0\1\3\1\0\6\3\4\0\4\3\1\0"+
    "\3\3\2\0\3\3\6\0\4\3\1\141\2\0\1\36"+
    "\3\3\1\0\1\3\1\0\1\3\1\0\6\3\4\0"+
    "\4\3\1\0\3\3\2\0\3\3\6\0\5\3\2\0"+
    "\1\36\1\3\1\142\1\3\1\0\1\3\1\0\1\3"+
    "\1\0\6\3\4\0\4\3\1\0\3\3\2\0\3\3"+
    "\6\0\5\3\2\0\1\36\3\3\1\0\1\3\1\0"+
    "\1\3\1\0\6\3\4\0\4\3\1\0\2\3\1\143"+
    "\2\0\3\3\6\0\1\3\1\144\3\3\2\0\1\36"+
    "\3\3\1\0\1\3\1\0\1\3\1\0\6\3\4\0"+
    "\4\3\1\0\3\3\2\0\3\3\6\0\5\3\2\0"+
    "\1\36\1\3\1\131\1\3\1\0\1\3\1\0\1\3"+
    "\1\0\6\3\4\0\4\3\1\0\3\3\2\0\3\3"+
    "\26\0\1\145\34\0\5\3\2\0\1\36\3\3\1\0"+
    "\1\3\1\0\1\3\1\0\3\3\1\146\2\3\4\0"+
    "\4\3\1\0\3\3\2\0\3\3\6\0\5\3\2\0"+
    "\1\36\3\3\1\0\1\3\1\0\1\3\1\0\3\3"+
    "\1\147\2\3\4\0\4\3\1\0\3\3\2\0\3\3"+
    "\10\0\3\150\5\0\1\150\3\0\1\150\3\0\2\150"+
    "\11\0\1\150\20\0\1\151\71\0\1\152\36\0\5\3"+
    "\2\0\1\36\3\3\1\0\1\3\1\0\1\3\1\0"+
    "\6\3\4\0\4\3\1\0\3\3\2\0\2\3\1\153"+
    "\6\0\5\3\2\0\1\36\3\3\1\0\1\3\1\0"+
    "\1\3\1\0\6\3\4\0\1\154\3\3\1\0\3\3"+
    "\2\0\3\3\6\0\5\3\2\0\1\36\3\3\1\0"+
    "\1\3\1\0\1\3\1\0\1\155\5\3\4\0\4\3"+
    "\1\0\3\3\2\0\3\3\6\0\5\3\2\0\1\36"+
    "\1\3\1\156\1\3\1\0\1\3\1\0\1\3\1\0"+
    "\6\3\4\0\4\3\1\0\3\3\2\0\3\3\6\0"+
    "\5\3\2\0\1\36\3\3\1\0\1\3\1\0\1\3"+
    "\1\0\6\3\4\0\4\3\1\0\1\157\2\3\2\0"+
    "\3\3\6\0\5\3\2\0\1\36\2\3\1\160\1\0"+
    "\1\3\1\0\1\3\1\0\6\3\4\0\4\3\1\0"+
    "\3\3\2\0\3\3\6\0\5\3\2\0\1\36\3\3"+
    "\1\0\1\3\1\0\1\3\1\0\6\3\4\0\4\3"+
    "\1\0\2\3\1\77\2\0\3\3\10\0\3\3\5\0"+
    "\1\3\3\0\1\3\3\0\2\3\11\0\1\3\56\0"+
    "\1\161\15\0\5\3\2\0\1\36\1\162\2\3\1\0"+
    "\1\3\1\0\1\3\1\0\6\3\4\0\4\3\1\0"+
    "\3\3\2\0\3\3\6\0\5\3\2\0\1\36\3\3"+
    "\1\0\1\3\1\0\1\163\1\0\6\3\4\0\4\3"+
    "\1\0\3\3\2\0\3\3\6\0\1\3\1\164\3\3"+
    "\2\0\1\36\3\3\1\0\1\3\1\0\1\3\1\0"+
    "\6\3\4\0\4\3\1\0\3\3\2\0\3\3\6\0"+
    "\5\3\2\0\1\36\3\3\1\0\1\3\1\0\1\165"+
    "\1\0\6\3\4\0\4\3\1\0\3\3\2\0\3\3"+
    "\6\0\5\3\2\0\1\36\3\3\1\0\1\3\1\0"+
    "\1\3\1\0\3\3\1\166\2\3\4\0\4\3\1\0"+
    "\3\3\2\0\3\3\6\0\5\3\2\0\1\36\3\3"+
    "\1\0\1\3\1\0\1\3\1\0\4\3\1\167\1\3"+
    "\4\0\4\3\1\0\3\3\2\0\3\3\31\0\1\170"+
    "\31\0\5\3\2\0\1\36\3\3\1\0\1\3\1\0"+
    "\1\131\1\0\6\3\4\0\4\3\1\0\3\3\2\0"+
    "\3\3\6\0\5\3\2\0\1\36\3\3\1\0\1\3"+
    "\1\0\1\3\1\0\1\3\1\171\4\3\4\0\4\3"+
    "\1\0\3\3\2\0\3\3\6\0\5\3\2\0\1\36"+
    "\3\3\1\0\1\3\1\0\1\3\1\0\6\3\4\0"+
    "\3\3\1\162\1\0\3\3\2\0\3\3\6\0\5\3"+
    "\2\0\1\36\3\3\1\0\1\3\1\0\1\3\1\0"+
    "\1\3\1\172\4\3\4\0\4\3\1\0\3\3\2\0"+
    "\3\3\6\0\5\3\2\0\1\36\3\3\1\0\1\3"+
    "\1\0\1\3\1\0\1\3\1\173\4\3\4\0\4\3"+
    "\1\0\3\3\2\0\3\3\6\0\5\3\2\0\1\36"+
    "\3\3\1\0\1\3\1\0\1\174\1\0\6\3\4\0"+
    "\4\3\1\0\3\3\2\0\3\3\27\0\1\175\33\0"+
    "\1\3\1\176\3\3\2\0\1\36\3\3\1\0\1\3"+
    "\1\0\1\3\1\0\6\3\4\0\4\3\1\0\3\3"+
    "\2\0\3\3\6\0\1\3\1\177\3\3\2\0\1\36"+
    "\3\3\1\0\1\3\1\0\1\3\1\0\6\3\4\0"+
    "\4\3\1\0\3\3\2\0\3\3\6\0\5\3\2\0"+
    "\1\36\3\3\1\0\1\3\1\0\1\3\1\0\6\3"+
    "\4\0\2\3\1\200\1\3\1\0\3\3\2\0\3\3"+
    "\6\0\5\3\2\0\1\36\3\3\1\0\1\3\1\0"+
    "\1\3\1\0\5\3\1\101\4\0\4\3\1\0\3\3"+
    "\2\0\3\3\42\0\1\201\20\0\5\3\2\0\1\36"+
    "\3\3\1\0\1\3\1\0\1\3\1\0\6\3\4\0"+
    "\4\3\1\0\1\157\1\30\1\3\2\0\3\3\6\0"+
    "\5\3\2\0\1\36\3\3\1\0\1\3\1\0\1\3"+
    "\1\0\6\3\4\0\4\3\1\0\1\202\2\3\2\0"+
    "\3\3\6\0\5\3\2\0\1\36\3\3\1\0\1\3"+
    "\1\0\1\3\1\0\3\3\1\203\2\3\4\0\4\3"+
    "\1\0\3\3\2\0\3\3\31\0\1\204\31\0\5\3"+
    "\2\0\1\36\3\3\1\0\1\3\1\0\1\3\1\0"+
    "\3\3\1\205\2\3\4\0\4\3\1\0\3\3\2\0"+
    "\3\3\6\0\5\3\2\0\1\36\2\3\1\206\1\0"+
    "\1\3\1\0\1\3\1\0\6\3\4\0\4\3\1\0"+
    "\3\3\2\0\3\3\20\0\1\207\42\0\5\3\2\0"+
    "\1\36\3\3\1\0\1\3\1\0\1\3\1\0\4\3"+
    "\1\210\1\3\4\0\4\3\1\0\3\3\2\0\3\3"+
    "\6\0\5\3\2\0\1\36\3\3\1\0\1\3\1\0"+
    "\1\3\1\0\4\3\1\77\1\3\4\0\4\3\1\0"+
    "\3\3\2\0\3\3\32\0\1\137\30\0\5\3\2\0"+
    "\1\36\3\3\1\0\1\3\1\0\1\211\1\0\6\3"+
    "\4\0\4\3\1\0\3\3\2\0\3\3\6\0\5\3"+
    "\2\0\1\36\1\212\2\3\1\0\1\3\1\0\1\3"+
    "\1\0\6\3\4\0\4\3\1\0\3\3\2\0\3\3"+
    "\6\0\5\3\2\0\1\36\3\3\1\0\1\3\1\0"+
    "\1\3\1\0\1\3\1\213\4\3\4\0\4\3\1\0"+
    "\3\3\2\0\3\3\6\0\1\3\1\214\3\3\2\0"+
    "\1\36\3\3\1\0\1\3\1\0\1\3\1\0\6\3"+
    "\4\0\4\3\1\0\3\3\2\0\3\3\6\0\5\3"+
    "\2\0\1\36\3\3\1\0\1\3\1\0\1\3\1\0"+
    "\3\3\1\127\2\3\4\0\4\3\1\0\3\3\1\136"+
    "\1\0\3\3\5\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5760];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\4\1\1\11\11\1\2\11\6\1\1\11"+
    "\4\1\1\0\1\1\1\0\21\1\1\0\2\1\1\0"+
    "\1\1\1\0\2\1\1\0\15\1\1\0\2\1\1\0"+
    "\1\1\1\0\1\1\1\0\12\1\1\0\2\1\3\0"+
    "\5\1\1\11\2\1\2\0\1\11\6\1\1\0\6\1"+
    "\1\0\4\1\1\0\3\1\1\0\2\1\1\0\2\1"+
    "\1\0\5\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[140];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /* user code: */


	/**
	 * Constructor.  This must be here because JFlex does not generate a
	 * no-parameter constructor.
	 */
	public fr() {
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 * @see #addToken(int, int, int)
	 */
	private void addHyperlinkToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so, true);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int tokenType) {
		addToken(zzStartRead, zzMarkedPos-1, tokenType);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 * @see #addHyperlinkToken(int, int, int)
	 */
	private void addToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so, false);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param array The character array.
	 * @param start The starting offset in the array.
	 * @param end The ending offset in the array.
	 * @param tokenType The token's type.
	 * @param startOffset The offset in the document at which this token
	 *        occurs.
	 * @param hyperlink Whether this token is a hyperlink.
	 */
	public void addToken(char[] array, int start, int end, int tokenType,
						int startOffset, boolean hyperlink) {
		super.addToken(array, start,end, tokenType, startOffset, hyperlink);
		zzStartRead = zzMarkedPos;
	}


	/**
	 * {@inheritDoc}
	 */
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return null;
	}


	/**
	 * Returns the first token in the linked list of tokens generated
	 * from <code>text</code>.  This method must be implemented by
	 * subclasses so they can correctly implement syntax highlighting.
	 *
	 * @param text The text from which to get tokens.
	 * @param initialTokenType The token type we should start with.
	 * @param startOffset The offset into the document at which
	 *        <code>text</code> starts.
	 * @return The first <code>Token</code> in a linked list representing
	 *         the syntax highlighted text.
	 */
	public Token getTokenList(Segment text, int initialTokenType, int startOffset) {

		resetTokenList();
		this.offsetShift = -text.offset + startOffset;

		// Start off in the proper state.
		int state = Token.NULL;
		switch (initialTokenType) {
			/* No multi-line comments */
			/* No documentation comments */
			default:
				state = Token.NULL;
		}

		s = text;
		try {
			yyreset(zzReader);
			yybegin(state);
			return yylex();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new TokenImpl();
		}

	}


	/**
	 * Refills the input buffer.
	 *
	 * @return      <code>true</code> if EOF was reached, otherwise
	 *              <code>false</code>.
	 */
	private boolean zzRefill() {
		return zzCurrentPos>=s.offset+s.count;
	}


	/**
	 * Resets the scanner to read from a new input stream.
	 * Does not close the old reader.
	 *
	 * All internal variables are reset, the old input stream 
	 * <b>cannot</b> be reused (internal buffer is discarded and lost).
	 * Lexical state is set to <tt>YY_INITIAL</tt>.
	 *
	 * @param reader   the new input stream 
	 */
	public final void yyreset(Reader reader) {
		// 's' has been updated.
		zzBuffer = s.array;
		/*
		 * We replaced the line below with the two below it because zzRefill
		 * no longer "refills" the buffer (since the way we do it, it's always
		 * "full" the first time through, since it points to the segment's
		 * array).  So, we assign zzEndRead here.
		 */
		//zzStartRead = zzEndRead = s.offset;
		zzStartRead = s.offset;
		zzEndRead = zzStartRead + s.count - 1;
		zzCurrentPos = zzMarkedPos = zzPushbackPos = s.offset;
		zzLexicalState = YYINITIAL;
		zzReader = reader;
		zzAtBOL  = true;
		zzAtEOF  = false;
	}




  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public fr(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public fr(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 212) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public org.fife.ui.rsyntaxtextarea.Token yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = zzLexicalState;


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 11: 
          { addToken(Token.RESERVED_WORD);
          }
        case 14: break;
        case 1: 
          { addToken(Token.IDENTIFIER);
          }
        case 15: break;
        case 9: 
          { addToken(Token.LITERAL_NUMBER_FLOAT);
          }
        case 16: break;
        case 13: 
          { addToken(Token.RESERVED_WORD_2);
          }
        case 17: break;
        case 4: 
          { addToken(Token.WHITESPACE);
          }
        case 18: break;
        case 6: 
          { addToken(Token.DATA_TYPE);
          }
        case 19: break;
        case 8: 
          { addToken(Token.ERROR_NUMBER_FORMAT);
          }
        case 20: break;
        case 10: 
          { addToken(Token.LITERAL_NUMBER_HEXADECIMAL);
          }
        case 21: break;
        case 5: 
          { addToken(Token.OPERATOR);
          }
        case 22: break;
        case 2: 
          { addToken(Token.LITERAL_NUMBER_DECIMAL_INT);
          }
        case 23: break;
        case 12: 
          { addToken(Token.LITERAL_BOOLEAN);
          }
        case 24: break;
        case 3: 
          { addNullToken(); return firstToken;
          }
        case 25: break;
        case 7: 
          { addToken(Token.SEPARATOR);
          }
        case 26: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            switch (zzLexicalState) {
            case YYINITIAL: {
              addNullToken(); return firstToken;
            }
            case 141: break;
            default:
            return null;
            }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
