
    /*ษออออออออออออออออออออออออออออออออออออออออออออออออออออออออออป
      บ                 "In The Name Of GOD" 		         บ
      บ   File Name :   Project1.cpp			         บ
      บ   Progarm   :   Arithmetic operations for binary numbers บ
      บ   Compiler  :   C++				         บ
      บ   Auther    :   Ali Javadzadeh Boloori 7913801           บ
      บ   Date      :   1380.4.17  	   	   	         บ
      ศออออออออออออออออออออออออออออออออออออออออออออออออออออออออออผ*/
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
//Preprocessing Directions:
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
#include<dos.h>
#include"strings.h"
#include<stdio.h>
#include<stdlib.h>
#include<conio.h>
#define LEN 32
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
//Global Datas:
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
int endp=0;   // End Of Program
int pos[6][2]={{5,4},{5,6},{5,8},{5,10},{5,12},{5,14}};
char text[6][16]={"Add","Subtract","Multiply","Division","Floating Point",
		  "Quit"};
String ONE("01");
String ZERO('0',1);
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
//Function Prototypes:
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String Add(String,String,String=ZERO,int=LEN);
void   Cls(void);
void   Division(String,String,String&,String&);
void   Exception(int);
void   Input(String&,String&);
int    MainMenu(void);
String Multiply(String,String);
void   Output(String&,String&,String&,char);
void   PrintTable(int,int,int,int);
char   ReadKey(void);
void   ShowMainMenu(void);
void   Sign_Extend(String&,int=LEN);
void   Start(void);
String Sub(String,String,int=LEN );
void   Text(void);
void   WritePos(int,int,int);
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
//Main Body of Program:
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
void main(void)
{
 clrscr();
 ShowMainMenu();
 MainMenu();
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
//Function Definitions:
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String Add(String x,String y,String carryin,int len)
{
 String sum('0',1),carryout('0',1),a('0',1),b('0',1),result('0',len);
 if(x[0]=='0'&&y[0]=='0'&&x[1]=='1'&&y[1]=='1'){Exception(1);exit(0);}
 for(int i=0;i<len;i++,x>>=1,y>>=1){
  a[0]=x[LEN-1];
  b[0]=y[LEN-1];
  sum=(a&~b&~carryin)|(~a&b&~carryin)|(~a&~b&carryin)|(a&b&carryin);
  carryout= ((a&carryin)|(b&carryin)|(a&b));
 // if(a[0]=='0'&&b[0]=='0'&&sum[0]=='1'&&i==7) Exception(1);
  result[len-i-1]=sum[0];
  //if(result>Max) Exception(1);
  carryin=carryout;
 }
 return result;
 }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
void Cls(void)
{
 gotoxy(30,5);
 textattr(0x00);
 for(int i=0;i<11;i++){
   gotoxy(30,5+i);
   for(int j=0;j<49;j++)
     cprintf("Û");
   }
 for(i=0;i<3;i++){
   gotoxy(4,18+i);
   for(int j=0;j<75;j++)
     cprintf("Û");
 }
 textattr(0x0f);
}
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
void Division(String dividend,String divisor,String&q,String&r)
{

  String remainder('0',2*LEN);
  String temprem('0',LEN);
  for(int j=0;j<LEN;j++)
   if(divisor[j]=='1')break;
  if(j==LEN) Exception(3);
  char sign1='0',sign2='0';
  if(dividend[0]=='1')sign2='1';
  if(divisor[0]!=dividend[0]) sign1='1';
  if(dividend[0]=='1')dividend=Add(~dividend,ONE);
  if(divisor[0]=='1')divisor=Add(~divisor,ONE);
  for (int i=1;i<LEN;i++)
     remainder[2*LEN-i]=dividend[LEN-i];
  remainder<<=1;
  for(i=0;i<LEN;i++){
    for (j=0;j<LEN;j++)
      temprem[j]=remainder[j];
  temprem=Sub(temprem,divisor);
  for (j=0;j<LEN;j++)
     remainder[j]=temprem[j];
  if (remainder[0]=='0') {
    remainder<<=1;
    remainder[2*LEN-1]='1';
  }
  else{
       temprem=Add(divisor,temprem);
       for (int j=0;j<LEN;j++)
       remainder[j]=temprem[j];
       remainder<<=1;
  }
 }
 for (j=0;j<LEN;j++)
   temprem[j]=remainder[j];
 temprem>>=1;
 for (j=0;j<LEN;j++)
   remainder[j]=temprem[j];
 for(i=1;i<LEN;i++){
   q[LEN-i]=remainder[2*LEN-i];
   r[i]=temprem[i];
 }
 if(sign1=='1')q=Add(~q,ONE);
 for(i=1;i<LEN;i++)
   if(r[i]!='0') break;
 if(i!=LEN)
  if(sign2=='1')r=Add(~r,ONE);
}
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
void Exception(int e)
{
 textattr(0x0f);
 gotoxy(31,8);
 switch (e)
  {
    case 1:{
	    cprintf("Overflow detected!");
	    ReadKey();ReadKey();
	    exit(0);
	    break;
    }

    case 2:{
	    cprintf("Underflow detected!");
	    ReadKey();ReadKey();
	    exit(0);
	    break;
    }

    case 3:{
	    cprintf("Divided by zero!");
	    ReadKey();ReadKey();
	    exit(0);
	    break;
    }

  }
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Input(String&a,String&b)
{
 char c[LEN+1],d[LEN+1];
 Sign_Extend(ZERO);
 Sign_Extend(ONE);
 PrintTable(29,4,79,16);
 gotoxy(31,6);
 textattr(0x07);
 cprintf("Enter Operand 1:");
 cscanf("%s",c);
 gotoxy(31,7);
 cprintf("Enter Operand 2:");
 cscanf("%s",d);
 if(strlen(c)>LEN||strlen(d)>LEN) Exception(1);
 a=c;
 b=d;
 Sign_Extend(a);
 Sign_Extend(b);
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

/*String  Multiply(String mccand,String mcplier)
{
 String product('0',2*LEN);
 char sign='0';
 if(mccand[0]!=mcplier[0]')sign='1';
 if (mccand[0]=='1')mccand=Add(~mccand,"1");
 if (mcplier[0]=='1')mcplier=Add(~mcplier,"1");
 Sign_Extend(mccand,2*LEN);
 char sign='0';
 for(int i=0;i<LEN;i++){
   if(mcplier[LEN-1]=='1')product=Add(product,mccand2,"0",2*LEN);
   mccand2<<=1;
   mcplier>>=1;
 }
if(sign=='1')product=Add(~product,"1","0",2*LEN);//product=Add(~product,"1");
return product;
} */
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String Multiply(String mccand,String mcplier)
{
 String product('0',2*LEN+1);
 String temp('0',LEN);
 for(int i=0;i<LEN;i++)
   product[2*LEN-i]=mcplier[LEN-i-1];
 product<<=1;
 for(i=0;i<LEN;i++){
   for(int j=0;j<LEN;j++)
   temp[j]=product[j];
   if(product[2*LEN]=='0'&& product[2*LEN-1]=='1'){
     temp=Sub(temp,mccand,LEN);
     for(int j=0;j<LEN;j++)
       product[j]=temp[j];
   }
   else if (product[2*LEN]=='1'&&product[2*LEN-1]=='0'){
	  temp=Add(temp,mccand);
	for(int j=0;j<LEN;j++)
	  product[j]=temp[j];
	}
   product>>=1;
   product[0]=product[1];
 }
 String result('0',2*LEN);
 for(i=0;i<2*LEN;i++)
  result[i]=product[i];
 return result;
}
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
int MainMenu(void)
{
 int position=0;
 String a('0',LEN),b('0',LEN),result('0'),q('0',LEN),r('0',LEN);
 char qo[LEN+1],re[LEN+1];
 char choice;
 Cls();
 while(!endp){
   WritePos(position,15,7);
   choice=ReadKey();
   switch (choice) {
	case 80:{
		  WritePos(position++,15,0);
		  if(position==6)position=0;
		  break;
	}
	case 72:{
		  WritePos(position--,15,0);
		  if(position==-1)position=5;
		  break;
	}

	case 13:{
	 switch(position){
	   case 0 : {
		       Input(a,b);
		       result=Add(a,b);
		       if(result[0]=='1'&&a[0]=='0'&&b[0]=='0'){
			 Exception(1);
			 return 0;
		       }
		       if(result[0]=='0'&&a[0]=='1'&&b[0]=='1'){
			Exception(1);
			return 0;
		       }
		       Output(result,a,b,'+');
		       ReadKey();ReadKey();
		       WritePos(position,15,0);
		       MainMenu();
		       break;
		    }
	   case 1 : {
		       Input(a,b);
		       result=Sub(a,b);
		       if( result[0]=='0' && a[0]=='1' && b[0]=='1' ){
			 Exception(1);
			 return 0;
		       }
		       Output(result,a,b,'-');
		       ReadKey();ReadKey();
		       WritePos(position,15,0);
		       MainMenu();
		       break;
	   }
	   case 2 : {
		       Input(a,b);
		       result=Multiply(a,b);
		       Output(result,a,b,'*');
		       ReadKey();ReadKey();
		       WritePos(position,15,0);
		       MainMenu();
		       break;

	   }
	   case 3 : {
		       Input(a,b);
		       Division(a,b,q,r);
		       Output(result,a,b,'%');
		       for(int i=0;i<LEN;i++){
			 qo[i]=q[i];
			 re[i]=r[i];
		       }
		       re[i]=0;qo[i]=0;
		       textattr(0x0f);
		       gotoxy(33,14);
		       cprintf("Qoutient :");
		       gotoxy(44,14);
		       cprintf("%s",qo);
		       gotoxy(33,15);
		       cprintf("Remainder:");
		       gotoxy(44,15);
		       cprintf("%s",re);
		       ReadKey();ReadKey();
		       WritePos(position,15,0);
		       MainMenu();
		       break;
	      }

	   case 4 :{

		   }
	   case 5:{
		   endp=1;
	  }
     }
    }
   }
 }
 return 0;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void  Output(String& result,String&a,String&b,char op)
{
 char answer[2*LEN+1],c[LEN+1],d[LEN+1];
 int len=LEN;
 for(int i=0;i<result.lenght();i++)
   answer[i]=result[i];
 answer[i]=0;
 for(i=0;i<LEN;i++){
   c[i]=a[i];
   d[i]=b[i];
 }
 c[i]=0;
 d[i]=0;
 textattr(0x0f);
 gotoxy(33,10);
 cprintf("%s",c);
 gotoxy(31,11);
 cprintf("%c\n",op);
 gotoxy(33,12);
 cprintf("%s",d);
 gotoxy(33,13);
 if(op!='*') for( i=0;i<LEN;i++)
	       cprintf("-");
 else {
	if(2*len<64){
	   for( i=0;i<2*LEN;i++)
	   cprintf("-");
	   gotoxy(33,14);
	   cprintf("%s",answer);
	 }
	 else{
	      PrintTable(4,18,78,20);
	      gotoxy(5,19);
	      textattr(0x0f);
	      cprintf("Product:%s",answer);
	}
 }
 if(op!='%'&&op!='*'){
   gotoxy(33,14);
   cprintf("%s",answer);
 }

}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void PrintTable(int x1,int y1,int x2,int y2)
{
   textattr(0x08);
   gotoxy(x1,y1);
   cprintf("ษ");
   for(int i=1;i<x2-x1;i++)  cprintf("อ");
   cprintf("ป");

   gotoxy(x1,y2);
   cprintf("ศ");
   for(i=1;i<x2-x1;i++)  cprintf("อ");
   cprintf("ผ");

   for(i=1;i<y2-y1;i++)
    {
     gotoxy(x2,y2-i);
     cprintf("บ");
     gotoxy(x1,y2-i);
     cprintf("บ");
    }
}
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
char ReadKey(void)
{
  int c=0;
  while(!c) c=getch();
  return c;
}
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
void ShowMainMenu(void)
{
 PrintTable(2,3,25,16);
 Text();
}
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
void Sign_Extend(String& s,int len)
{
 String t('0',len);
 int l;
 l= s.lenght();
 if(s[0]=='0'){
		for(int i=1;i<=l-1;i++)
		   t[len-i]=s[l-i];
		t[0]=s[0];
		s=t;
 }
 else {
       String temp=~s;
       for(int i=0;i<=l-1;i++)
	   t[len-i-1]=temp[l-i-1];
       t=~t;
       s=t;
 }
}
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
String Sub(String x,String y,int len)
{
 return Add(x,~y,ONE,len);
}
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
void Text(void)
{
 textattr(0x0f);
 for(int i=0;i<6;i++){
   gotoxy(pos[i][0],pos[i][1]);
   cprintf(text[i]);
 }
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void WritePos(int p,int col,int cursor)
{
 //Max of col=15,Max of cursor=7
 gotoxy(pos[p][0],pos[p][1]);
 textbackground(0);
 textattr(cursor);
 for(int i=0;i<16;i++)
 cprintf("Û");
  gotoxy(pos[p][0],pos[p][1]);
 textattr(col|(cursor<<4));
 cprintf(text[p]);
}