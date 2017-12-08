
    /*************************************************************************
      *                 "In The Name Of GOD" 		                                                
	  *   File Name :   Project3.cpp			         
	  *   Progarm   :   Show the weekly plan of a student        
      *   Compiler  :   Borland C++	3.1			         
      *   Auther    :   Ali Javadzadeh Boloori 7913801           
	  *   Date      :   1380.5.3  	   	   	         
     *************************************************************************/
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
//Preprocessing Directions:
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
#include <conio.h>
#include <ctype.h>
#include <dos.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fstream.h>
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
//Global Datas:
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
const LEN=15,N_OF_G=9,AGE_LEN=3,AV_LEN=6,DAY_LEN=11,HOUR_LEN=7,PU_LEN=5,
      MAX_TEXT=5,MAX_REC=100;
int  pos[6][2]={{3,24},{20,24},{35,24},{48,24},{63,24},{74,24}};
char texts[6][10]={"Insert St","Insert Pr","Add Text","Search","Sort","Quit"};
char lables[MAX_REC];
int  numofrecs=0;
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
//Class Interface:
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
class Date
{
 public:
  Date(){day[0]=hour[0]=0;}
  void Normal(void);
  char day[DAY_LEN];
  char hour[HOUR_LEN];
};
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
class Fields
{
 public:
  friend int  operator>(Fields&,Fields&);
  Fields& operator=    (const Fields&);
  char lable;
  char name[LEN];
  char surname[LEN];
  int  number;
};
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
class Group
{
 public:
  Group(){number[0]=0,professor[0]=0;}
  void Normal(void);
  char professor[LEN];
  char number[2];
  Date date[2];
};
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
class Lesson
{
 public:
  friend  int operator>(Lesson&,Lesson&);
 Lesson  (){group[0]=professor[0]=hour[0]=name[0]=day[0]=0;}
 Lesson& operator=     (const Lesson&);
 void    Normal        (void);
 void    Print         (int);
 char    day[DAY_LEN];
 char    group[2];
 char    hour[HOUR_LEN];
 char    name[LEN];
 char    professor[LEN];
};
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
class Person{
 public:
   char name[LEN];
   char surname[LEN];
   virtual void  Normal()=0;
   virtual void  Get   ()=0;
   virtual void  Insert()=0;
   virtual void  Show  ()=0;
   virtual void  Texts ()=0;
	   char*  Num   (){return num;}
	   char* Text_ (int i=0){return text[i];}
	   char  Group_(int i=0){return groups[i];}
 protected:
   char age[AGE_LEN];
   char city[LEN];
   char department[LEN];
   char groups[MAX_TEXT+1];
   char num[2];
   char phonenumber[LEN];
   char sex[2];
   char text[MAX_TEXT][LEN];
};
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
class Professor:public Person{
 public:
   void Get();
   void Insert  (void);
   void Normal  (void);
   void Print_T (void);
   void Show    ();
   void Texts   ();
 private:
   char degree[LEN];
   char salary[LEN];
   char start_teaching[5];
};
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
class Student:public Person{
 public:
   void Get();
   void Insert        (void);
   void Normal        (void);
   void Print_Plan    (void);
   void Show          (void);
   void Texts         (void);
 private:
   char average[AV_LEN];
   char course[3];
   char number[LEN];
   char passed_units[PU_LEN];
   char rank[LEN];
};
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
class Text{
 public:
   void Get      (void);
   void Insert   (void);
   void Normal   (void);
   char Num      (){return num[0];}
   void Texts    (void);
   char Unit     (){return unit;}
   char name[LEN];
   char num[2];
   Group group[N_OF_G];
 private:
   char code[LEN];
   char unit;
};
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
//Function Prototypes:
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
int  Binary_Search (char*,char*);
void Clean         (void);
void Cls           (int);
void Copy_Files    (void);
void Correct       (char[],char);
void Lables_Read   (void);
void Lables_Write  (void);
int  Location      (int);
int  MainMenu      (void);
void Message       (int);
void MessageWindow (void);
int  NumOfRecs     (void);
int  Precedence    (char*);
void PrintTable    (int,int,int,int,int,int);
char ReadKey       (void);
void Screen        (void);
void Search        (void);
int  Search_Text   (char*,Text*);
void Sort_File     (void);
void Start         (void);
void StrToLen      (char *,char[],int,char);
void Texts         (void);
void WritePose     (int,int);
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
//Generic Function:
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
template <class T>
void Sort           (T*,int);
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
//Main Body Of Program:
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void main()
{
 Start();
 Screen();
 MainMenu();
 _setcursortype(_NORMALCURSOR);
 textattr(0x07);
 return;
 }
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
//Function Definitions:
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
int  Binary_Search  (char* name,char* surname)
{
 Student a;
 Professor b;
 char n[LEN],s[LEN],lable;
 int left=1,right=numofrecs,location,notfound=1;;
 ifstream in;
 in.open("Data.txt",ios::in);
 if(!in) {Message(2);return 0;}
 while(notfound&&left<=right)
  {
      location=(left+right)/2;
      in.seekg(Location(location),ios::beg);
      if(lables[location-1]=='S'){
	in.read((char*)&a,sizeof(Student));
	strcpy(n,a.name);
	strcpy(s,a.surname);
      }
     else{
	  in.read((char*)&b,sizeof(Professor));
	  strcpy(n,b.name);
	  strcpy(s,b.surname);
     }
     Correct(s,'c');
     Correct(n,'c');
     notfound=strcmp(surname,s);
     if(notfound>0)left=location+1;
     else {
	   if(notfound<0)right=location-1;
	   else{
		notfound=strcmp(name,n);
		if(notfound>0) left=location+1;
		if(notfound<0) right=location-1;
	   }
     }
  }
 if(!notfound){
		if(lables[location-1]=='P'){
		   Clean();
		   b.Texts();
		   b.Show();
		   b.Print_T();
		}
		else{
		   Clean();
		   a.Texts();
		   a.Show();
		   a.Print_Plan();
	       }
 }
 else Message(1);
 in.close();
 return 0;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void  Clean  (void)
{
 textattr(0x11);
 gotoxy(3,3);
 int i;
 for( i=0;i<20;i++){
   for(int j=0;j<27;j++)
     cprintf("Û");
   gotoxy(3,3+i);
 }
 gotoxy(44,3);
 for(i=0;i<11;i++){
  for(int j=0;j<11;j++)
     cprintf("Û");
 gotoxy(44,3+i);
  }
 gotoxy(56,3);
 for(i=0;i<11;i++){
  for(int j=0;j<5;j++)
     cprintf("Û");
 gotoxy(56,3+i);
  }
   gotoxy(32,3);
 for(i=0;i<11;i++){
  for(int j=0;j<10;j++)
     cprintf("Û");
 gotoxy(32,3+i);
  }
  gotoxy(62,3);
 for(i=0;i<11;i++){
  for(int j=0;j<11;j++)
     cprintf("Û");
 gotoxy(62,3+i);
  }
  gotoxy(74,3);
 for(i=0;i<11;i++){
  for(int j=0;j<6;j++)
     cprintf("Û");
 gotoxy(74,3+i);
  }
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void  Cls(int color)
{
 clrscr();
 textattr(color);
 for(int i=0;i<26;i++){
  for(int j=0;j<81;j++)
   {
   gotoxy(j,i);
   cprintf("Û");
   }
 }
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Copy_Files (void)
{
 Student a;
 Professor b;
 char lable;
 ifstream in;
 ofstream out;
 in.open("Data.swp",ios::in);
 out.open("Data.txt",ios::out);
 for (int i=0;i<numofrecs;i++){
   if(lables[i]=='S'){
       in.read((char*)&a,sizeof(Student));
       out.write((char*)&a,sizeof(Student));
   }
   else{
       in.read((char*)&b,sizeof(Professor));
       out.write((char*)&b,sizeof(Professor));
   }
   in.get();
   out.put('\n');
 }
 in.close();
 out.close();
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 void Correct(char s[],char flag)
{
 char p[LEN];
 int i=0,j=0;
 if(flag=='n'){
	       while(s[i]=='0')i++;
	       for(;s[i]!=NULL;i++,j++) p[j]=s[i];
 }
 else for(;s[i]!=' '&&i<LEN-1;j++,i++) p[j]=s[i];
 p[j]=0;
 strcpy(s,p);
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Lables_Read(void)
{
 ifstream in("Lables.txt",ios::in);
 if(!in)  Message(2);
 in.get(lables[0]);
 for(int i=1;!in.eof();i++)
    in.get(lables[i]);
 in.close();

}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Lables_Write(void)
{
 ofstream out("Lables.txt",ios::out);
 if(!out)  Message(2);
 for(int i=0;i<numofrecs;i++)
   out.put(lables[i]);
 out.close();
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
int Location (int location)
{
 int p=0,s=0,sum=0;
 char lable;
 for(int i=0;i<location-1;i++){
    if(lables[i]=='P') p++;
    else s++;
 }
 sum=s*(sizeof(Student)+2)+p*(sizeof(Professor)+2);
 return sum;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
int MainMenu(void)
{
 int position=0,end=0, c;
 numofrecs=NumOfRecs();
 Lables_Read();
 for(int i=0;i<6;i++)
  WritePose(i,0x30);
  while(!end)
  {
    WritePose(position,0x3b);
    c=ReadKey();
    switch (c)
    {
      case 77        :   WritePose(position++,0x30);
			 if (position==6) position=0;
			 break;
      case 75        :   WritePose(position--,0x30);
			 if (position==-1) position=5;
			 break;
      case 13        :
       {
	 if(position!=5){
	  // sound (1000+position);
	   //delay(100);
	   //nosound();
         }
	 switch(position){
	      case 0 :
	       {
		 Student a;
		 a.Texts();
		 a.Get();
		 a.Normal();
		 a.Insert();
		 Clean();
		 ReadKey();
		 break;
	       }
	      case 1 :
	       {
		 Professor b;
		 b.Texts();
		 b.Get();
		 b.Normal();
		 b.Insert();
		 Clean();
		 ReadKey();
		 break;
	       }
	      case 2 :
	       {
		Text c;
		c.Texts();
		c.Get();
		c.Normal();
		c.Insert();
		Clean();
		ReadKey();
		break;
	       }
	      case 3 :
	       {
		 //Sort_File();
		 Search();
		 ReadKey();
		 ReadKey();
		 Clean();
		 break;
	       }
	      case 4 :
	      {
	       Message(6);
	       Sort_File();
	       Message(7);
	       ReadKey();
	       Clean();
	       break;
	      }
	      case 5 :
		Lables_Write();
		Cls(0);
		_setcursortype(_NORMALCURSOR);
		Message(8);
		_setcursortype(_NORMALCURSOR);
		return 0;

	 }
    }
  }
 }
 return 1;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Message(int m)
{
 textattr(0x9f);
 if(m!=8&&m!=1)
 {
   //sound(1500);
  // delay(100);
   //nosound();
 }
 switch(m){
      case 1:
       {
	 gotoxy(3,16);
	 cprintf("Name  Not Found!");
	 //sound(1300);
	// delay(200);
 //	 sound(900);
	// delay(200);
	// sound(700);
	 // delay(200);
	// nosound();
	 break;
       }
      case 2:
       {
	 gotoxy(3,17);
	 cprintf("Can't Open The File");
	 break;
       }
     case 3:
       {
	 gotoxy(3,18);
	 cprintf("Text  Not Found");
	 break;
       }

      case 4:
       {
	 gotoxy(3,19);
	 cprintf("Group Not Found");
	 break;
       }

     case 5:
       {
	 gotoxy(3,20);
	 cprintf("Interferenc Between Groups!");
	 break;
       }
     case 6:
      {
       gotoxy(3,10);
       textattr(0x1f);
       cprintf("Sorting The File......");
      // delay(500);
       gotoxy(3,10);
       textattr(0x9f);
       cprintf("The File Is Sorted Now!");
       break;
      }
     case 7:
      {
       gotoxy(3,10);
       cprintf("The File Is Sorted Now!");
       break;
      }
     case 8:
     {
      PrintTable(1,1,37,5,7,0);
      gotoxy(2,2);
      cprintf(" Thanks For Running This  Program!");
      gotoxy(2,3);
      cprintf(" Ali Javadzadeh Boloori   7913801. ");
      gotoxy(2,4);
      cprintf(" 25 July 2001.");
      _setcursortype(_NORMALCURSOR);
      /*sound(300);
      delay(400);
      sound(400);
      delay(400);
      sound(500);
      delay(400);
      nosound();*/
      break;
     }

 }
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
int  NumOfRecs     (void)
{
 int sum=0;
 fstream in;
 in.open("lables.txt",ios::in|ios::out);
 while (!(in.eof())){
  in.get();
  sum++;
 }
 in.close();
 return sum-1;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
int  Precedence    (char* day)
{
 if (!strcmp(day,"Saturday")) return 1;
 if (!strcmp(day,"Sunday"))   return 2;
 if (!strcmp(day,"Monday"))   return 3;
 if (!strcmp(day,"Tuesday"))  return 4;
 if (!strcmp(day,"Wednesday"))return 5;
 if (!strcmp(day,"Thursday")) return 6;
 if (!strcmp(day,"Friday"))   return 7;
 return 0;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void PrintTable(int x1,int y1,int x2,int y2,int color,int back)
{
   textattr(color|(back<<4));
   gotoxy(x1,y1);
   cprintf("É");
   int i;
   for( i=1;i<x2-x1;i++)  cprintf("Í");
   cprintf("»");
   gotoxy(x1,y2);
   cprintf("È");
   for(i=1;i<x2-x1;i++)  cprintf("Í");
   cprintf("¼");
   for(i=1;i<y2-y1;i++)
    {
     gotoxy(x2,y2-i);
     cprintf("º");
     gotoxy(x1,y2-i);
     cprintf("º");
    }
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
char ReadKey(void)
{  char c=0;
   while(!c) c=getch();
   return c;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Screen(void)
{
  int i,j;
  window(1,1,80,25);
  clrscr();
  textattr(1);
  gotoxy(0,0);
  for ( i=0 ;i<=26;i++)
    for ( j=0 ;j<80;j++)
       cprintf("Û");
  PrintTable(2,1,30,23,11,1);
  PrintTable(31,1,80,23,11,1);
  Texts();
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Search(void)
{
 char c;
 char name[LEN],surname[LEN],tem1[LEN],tem2[LEN];
 Student a;
 Professor b;
 textattr(0x1b);
 gotoxy(3,3);
 cprintf("Name   :");
 textattr(0x1f);
 cprintf("¯");
 cscanf("%s",name);
 name[0]=toupper(name[0]);
 gotoxy(3,4);
 textattr(0x1b);
 cprintf("SurName:");
 textattr(0x1f);
 cprintf("¯");
 cscanf("%s",surname);
 surname[0]=toupper(surname[0]);
 StrToLen(name,tem1,LEN,'0');
 StrToLen(surname,tem2,LEN,'0');
 Binary_Search(name,surname);
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
int Search_Text(char*name,Text*a)
{
 ifstream in;
 in.open("Texts.txt",ios::in);
 if(!in) Message(2);
 char temp[LEN];
 StrToLen(name,temp,LEN,'c');
 while(in){
     in.read((char*)a,sizeof(Text));
     if(!strcmp(temp,a->name)) {in.close();return 1;}
     in.get();
 }
 in.close();
 if(!strcmp(temp,a->name)) return 1;

 return 0;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
template <class T>
void Sort(T* a,int n)
{
 T temp;
 int i,j;
 for( i=1;i<n;i++){
   temp=a[i];
   for(j=i;j>0&&a[j-1]>temp;j--)
      a[j]=a[j-1];
   a[j]=temp;
 }
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Sort_File(void)
{
 char lable;
 int i;
 Fields fields[MAX_REC];
 Student a;
 Professor b;
 ifstream in;
 ofstream out;
 in.open ("Data.txt",  ios::in);
 out.open("Data.swp",  ios::out);
 if(!in||!out)Message(2);
 for( i=0;i<numofrecs;i++){
   if(lables[i]=='S'){
      in.read((char*)&a,sizeof(Student));
      in.get();
      strcpy(fields[i].name,a.name);
      strcpy(fields[i].surname,a.surname);
    }
   else{
      in.read((char*)&b,sizeof(Professor));
      in.get();
      strcpy(fields[i].name,b.name);
      strcpy(fields[i].surname,b.surname);
   }
   fields[i].number=i+1;
   fields[i].lable=lables[i];
 }
 Sort(fields,numofrecs);
 for(i=0;i<numofrecs;i++){
   in.seekg(Location(fields[i].number),ios::beg);
   if(fields[i].lable=='S'){
    in.read((char*)&a,sizeof(Student));
    out.write((char*)&a,sizeof(Student));
   }
   else{
    in.read((char*)&b,sizeof(Professor));
    out.write((char*)&b,sizeof(Professor));
   }
   out.put('\n');
 }
 for(i=0;i<numofrecs;i++)
    lables[i]=fields[i].lable;
 in.close();
 out.close();
 Lables_Write();
 Copy_Files();
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Start    (void)
{
  _setcursortype(_NOCURSOR);
  Cls(1);
  textattr(0x33);
  for (int i=0; i<15; i++)  {
    window(38-i,13-i/2,42+i,13+i/2);
    clrscr();
    //delay(25);
  }
  gotoxy(2,2);
  textattr(0x3e);
  cprintf("      In The Name Of God\n\n");
  textattr(0x3b);
  PrintTable(1,1,32,15,11,3);
  textattr(0x35);
  textattr(0x3a);
  gotoxy(10,4); cprintf(" *Project 3 *");
  gotoxy(2,6); cprintf(" This Program is Written By:");
  textattr(0x39);
  gotoxy(2,8); cprintf("       Ali Javadzadeh ");
  gotoxy(3,10); cprintf("          7913801");
  textattr(0x3e);
  gotoxy(2,13); cprintf("     Amirkabir University    ");
  getch();
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void StrToLen(char * in,char out[],int len,char flag)
{
   int i;
   if (flag=='n') {
     for (i=0; i<len-strlen(in)-1;i++) out[i]='0';
     for ( i=0;i<strlen(in)&& i<len-1;i++) out[len+i-strlen(in)-1]=in[i];
   }
   else {
     for ( i=0;i<strlen(in)&& i<len-2;i++) out[i]=in[i];
     for (; i<len-1;i++) out[i]=' ';
   }
   out[len-1]=0;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Texts(void)
{
 gotoxy(6,2);  textattr(0x1e); cprintf("Personal Information  ");textattr(0x1b);
 gotoxy(32,2); textattr(0x1e); cprintf("Day  ");   textattr(0x1b);
 gotoxy(43,1); cprintf("Ñ");
 gotoxy(43,2); cprintf("³");
 gotoxy(44,2); textattr(0x1e); cprintf("Text Name "); textattr(0x1b);
 gotoxy(55,1); cprintf("Ñ");
 gotoxy(55,2); cprintf("³");
 gotoxy(56,2); textattr(0x1e); cprintf("Group");       textattr(0x1b);
 gotoxy(61,1); cprintf("Ñ");
 gotoxy(61,2); cprintf("³");
 gotoxy(62,2); textattr(0x1e); cprintf(" Professor "); textattr(0x1b);
 gotoxy(73,2); cprintf("³");
 gotoxy(73,1); cprintf("Ñ");
 gotoxy(74,2); textattr(0x1e); cprintf("Hour");        textattr(0x1b);
 int i;
 for ( i=0;i<20;i++){
   gotoxy(43,3+i); cprintf("³");
 }
 for (i=0;i<20;i++){
   gotoxy(55,3+i); cprintf("³");
 }
 for (i=0;i<20;i++){
   gotoxy(61,3+i); cprintf("³");
 }
 for (i=0;i<20;i++){
   gotoxy(73,3+i); cprintf("³");
 }
  gotoxy(43,23);cprintf("Ï");
  gotoxy(55,23);cprintf("Ï");
  gotoxy(61,23);cprintf("Ï");
  gotoxy(73,23);cprintf("Ï");
  textattr(0x13);
  gotoxy(3,24); for(i=0;i<10;i++) cprintf("Û");//Insert St
  gotoxy(20,24);for(i=0;i<10;i++) cprintf("Û");//Insert Pr
  gotoxy(35,24);for(i=0;i<7;i++)  cprintf("Û");//Add Text
  gotoxy(48,24);for(i=0;i<7;i++)  cprintf("Û");//Search
  gotoxy(62,24);for(i=0;i<7;i++)  cprintf("Û");//Sort
  gotoxy(74,24);for(i=0;i<6;i++)  cprintf("Û");//Quit
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void WritePose(int p,int color)
{
  textattr(color);
  gotoxy(pos[p][0],pos[p][1]);
  cprintf(texts[p]);
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
//Class Implementation:
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Date::Normal(void)
{
 char temp[DAY_LEN];
 StrToLen(day,temp,DAY_LEN,'c');
 strcpy(day,temp);
 StrToLen(hour,temp,HOUR_LEN,'c');
 strcpy(hour,temp);
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
int operator>(Fields& a,Fields&b)
{
 if(strcmp(a.surname,b.surname)>0) return 1;
 if(strcmp(a.surname,b.surname)<0) return 0;
 if(strcmp(a.name,b.name)<0) return 0;
 if(strcmp(a.name,b.name)>0) return 1;
 return 0;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
Fields& Fields::operator=(const Fields& a)
{
 strcpy(name,a.name);
 strcpy(surname,a.surname);
 number=a.number;
 lable=a.lable;
 return *this;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Group::Normal(void)
{
 char temp[LEN];
 StrToLen(professor,temp,LEN,'c');
 strcpy(professor,temp);
 for(int i=0;i<2;i++)
    date[i].Normal();
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
Lesson& Lesson::operator=(const Lesson& a)
{
 strcpy(name,a.name);
 strcpy(day,a.day);
 strcpy(professor,a.professor);
 strcpy(hour,a.hour);
 group[0]=a.group[0];
 return *this;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
int operator>(Lesson& a, Lesson& b)
{
 if(Precedence(a.day)>Precedence(b.day))   return 1;
 if(Precedence(a.day)<Precedence(b.day))   return 0;
 if(strlen(a.hour)<strlen(b.hour))         return 0;
 if(strlen(a.hour)>strlen(b.hour))         return 1;
 if(strcmp(a.hour,b.hour)>0)               return 1;
 if(strcmp(a.hour,b.hour)<0)               return 0;
 if(a.hour[0]!=0)Message(5);
 return -1;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Lesson::Print(int x)
{
 textattr(0x1f);
 if(!group[0]){
 gotoxy(32,x);cprintf("NONE");
 gotoxy(44,x);Correct(name,'c');cprintf("%s",name);
 gotoxy(57,x);                  cprintf("NONE");
 gotoxy(63,x);Correct(name,'c');cprintf("NONE");
 gotoxy(74,x);Correct(name,'c');cprintf("NONE");
 }
 else{
      gotoxy(32,x);Correct(day,'c'); cprintf("%s",day);
      gotoxy(44,x);Correct(name,'c');cprintf("%s",name);
      gotoxy(58,x);                  cprintf("%c",group[0]);
      gotoxy(63,x);Correct(name,'c');cprintf("%s",professor);
      gotoxy(74,x);Correct(name,'c');cprintf("%s",hour);
 }
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Professor::Get()
{
 textattr(0x1f);
 int g,n;
 gotoxy(15,3);  cprintf("¯");  cscanf("%s",name);name[0]=toupper(name[0]);
 gotoxy(15,4);  cprintf("¯");  cscanf("%s",surname);surname[0]=toupper(surname[0]);
 gotoxy(15,5);  cprintf("¯");  cscanf("%s",age);age[AGE_LEN]=NULL;
 gotoxy(15,6);  cprintf("¯");  cscanf("%s",sex);
 gotoxy(15,7);  cprintf("¯");  cscanf("%s",phonenumber);
 gotoxy(15,8);  cprintf("¯");  cscanf("%s",city);
 gotoxy(15,9);  cprintf("¯");  cscanf("%s",department);
 gotoxy(15,10); cprintf("¯");  cscanf("%s",degree);
 gotoxy(15,11); cprintf("¯");  cscanf("%s",start_teaching);
 gotoxy(15,12); cprintf("¯");  cscanf("%s",salary);
 gotoxy(15,13); cprintf("¯");  cscanf("%s",num);
 n=atoi(num);
 int i;
  for(i=0;i<n;i++){
    gotoxy(44,3+i);  cprintf("¯");  cscanf("%s",text[i]);
    gotoxy(56,3+i);  cprintf("¯");  cscanf("%d",&g);
    groups[i]=g+'0';
  }
  for(i=n;i<MAX_TEXT;i++){
  groups[i]='0';
  text[i][0]=0;
 }
 groups[MAX_TEXT]=NULL;
 //text[MAX_TEXT][0]=NULL;

}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Professor ::Insert()
{
 fstream out;
 out.open("Data.txt",ios::app);
 if(!out) Message(2);
 out.write((char*)this,sizeof(Professor));
 out.put('\n');
 out.close();
 lables[numofrecs++]='P';
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Professor::Normal(void)
{
 char temp[LEN];
 StrToLen(name,temp,LEN,'c');
 strcpy(name,temp);
 StrToLen(surname,temp,LEN,'c');
 strcpy(surname,temp);
 StrToLen(phonenumber,temp,LEN,'n');
 strcpy(phonenumber,temp);
 StrToLen(city,temp,LEN,'c');
 strcpy(city,temp);
 StrToLen(department,temp,LEN,'c');
 strcpy(department,temp);
 StrToLen(degree,temp,LEN,'c');
 strcpy(degree,temp);
 StrToLen(salary,temp,LEN,'n');
 strcpy(salary,temp);
 StrToLen(start_teaching,temp,5,'n');
 strncpy(start_teaching,temp,5);
 StrToLen(groups,temp,MAX_TEXT+1,'n');
 int i;
 for(  i=0;i<atoi(num);i++){
   StrToLen(text[i],temp,LEN,'c');
   strcpy(text[i],temp);
 }
 for(i=atoi(num);i<MAX_TEXT;i++){
  text[i][0]=0;
  StrToLen(text[i],temp,LEN,'c');
  strcpy(text[i],temp);
 }
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void  Professor::Print_T(void)
{
 for(int i=0;i<atoi(num);i++){
   gotoxy(44,3+i);
   Correct(text[i],'c');
   cprintf("%s",text[i]);
   gotoxy(58,3+i);
   cprintf("%c",groups[0]);
 }
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Professor::Show()
{
 textattr(0x1f);
 gotoxy(16,3);  Correct(name,'c');          cprintf("%s",name);
 gotoxy(16,4);  Correct(surname,'c');       cprintf("%s",surname);
 gotoxy(16,5);  Correct(age,'n');           cprintf("%s",age);
 gotoxy(16,6);  if( !strcmp(sex,"m") || !strcmp(sex,"M") ) cprintf("Male");
		else cprintf("Female");
 gotoxy(16,7);  Correct(phonenumber,'n');   cprintf("%s",phonenumber);
 gotoxy(16,8);  Correct(city,'c');          cprintf("%s",city);
 gotoxy(16,9);  Correct(department,'c');    cprintf("%s",department);
 gotoxy(16,10); Correct(degree,'c');        cprintf("%s",degree);
 gotoxy(16,11); Correct(start_teaching,'n');cprintf("%s",start_teaching);
 gotoxy(16,12); Correct(salary,'n');        cprintf("%s",salary);
 gotoxy(17,13); cprintf("%s",num);
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Professor::Texts()
{
 Clean();
 textattr(0x1b);
 gotoxy(3,3);  cprintf ("Name       :");
 gotoxy(3,4);  cprintf ("Surname    :");
 gotoxy(3,5);  cprintf ("Age        :");
 gotoxy(3,6);  cprintf ("Sex(M-F)   :");
 gotoxy(3,7);  cprintf ("Phone      :");
 gotoxy(3,8);  cprintf ("City       :");
 gotoxy(3,9);  cprintf ("Department :");
 gotoxy(3,10); cprintf ("Degree     :");
 gotoxy(3,11); cprintf ("Teach since:");
 gotoxy(3,12); cprintf ("Salary     :");
 gotoxy(3,13); cprintf("NumberOfTexts:");
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Student::Get()
{
 textattr(0x1f);
 int n,g;
 gotoxy(16,3);  cprintf("¯");  cscanf("%s",name);name[0]=toupper(name[0]);
 gotoxy(16,4);  cprintf("¯");  cscanf("%s",surname);surname[0]=toupper(surname[0]);
 gotoxy(16,5);  cprintf("¯");  cscanf("%s",number);
 gotoxy(16,6);  cprintf("¯");  cscanf("%s",age);
 gotoxy(16,7);  cprintf("¯");  cscanf("%s",sex);
 gotoxy(16,8);  cprintf("¯");  cscanf("%s",phonenumber);
 gotoxy(16,9);  cprintf("¯");  cscanf("%s",city);
 gotoxy(16,10); cprintf("¯");  cscanf("%s",department);
 gotoxy(16,11); cprintf("¯");  cscanf("%s",rank);
 gotoxy(16,12); cprintf("¯");  cscanf("%s",course);
 gotoxy(16,13); cprintf("¯");  cscanf("%s",average);
 gotoxy(16,14); cprintf("¯");  cscanf("%s",passed_units);
 gotoxy(16,15); cprintf("¯");  cscanf("%s",num);
 n=atoi(num);
 int i;
 for( i=0;i<n;i++){
    gotoxy(44,3+i);  cprintf("¯");  cscanf("%s",text[i]);
    gotoxy(56,3+i);  cprintf("¯");  cscanf("%d",&g);
    groups[i]=g+'0';
  }
 for(i=n;i<5;i++)
  groups[i]='0';
 groups[MAX_TEXT]=NULL;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Student ::Insert()
{
 fstream out;
 out.open("Data.txt",ios::app);
 if(!out) Message(2);
 out.write((char*)this,sizeof(Student));
 out.put('\n');
 out.close();
 lables[numofrecs++]='S';
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Student::Normal(void)
{
 char temp[LEN];
 StrToLen(name,temp,LEN,'c');
 strcpy(name,temp);
 StrToLen(surname,temp,LEN,'c');
 strcpy(surname,temp);
 StrToLen(number,temp,LEN,'n');
 strcpy(number,temp);
 StrToLen(phonenumber,temp,LEN,'n');
 strcpy(phonenumber,temp);
 StrToLen(city,temp,LEN,'c');
 strcpy(city,temp);
 StrToLen(department,temp,LEN,'c');
 strcpy(department,temp);
 StrToLen(rank,temp,LEN,'c');
 strcpy(rank,temp);
 int i;
 for( i=0;i<atoi(num);i++){
   StrToLen(text[i],temp,LEN,'c');
   text[i][0]=0;
   strcpy(text[i],temp);
 }
 for(i=atof(num);i<MAX_TEXT;i++){
  text[i][0]=0;
  StrToLen(text[i],temp,LEN,'c');
  strcpy(text[i],temp);
 }

 StrToLen(age,temp,AGE_LEN,'n');
 strcpy(age,temp);
 StrToLen(average,temp,AV_LEN,'n');
 strcpy(average,temp);
 StrToLen(passed_units,temp,PU_LEN,'n');
 strcpy(passed_units,temp);
 StrToLen(course,temp,3,'n');
 strncpy(course,temp,3);
 StrToLen(groups,temp,MAX_TEXT+1,'n');
 strncpy(groups,temp,MAX_TEXT);
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Student::Print_Plan(void)
{

 int i,j,k;
 /*sound(700);
 delay(200);
 sound(900);
 delay(200);
 sound(1300);
  delay(200);
 nosound();     */
 Text text_;
 Lesson out[LEN];
 for(i=0,k=0;i<atof(num);i++,k++){
    if(Search_Text(text[i],&text_)){
      for( j=0;j<text_.num[0]-'0';j++)
       if(text_.group[j].number[0]==groups[i]) break;
	if(j!=text_.Num()-'0'){
	  strcpy(out[k].name,text_.name);
	  strcpy(out[k].day,text_.group[j].date[0].day);
	  out[k].group[0]=text_.group[j].number[0];
	   out[k].group[1]=0;
	  strcpy(out[k].professor,text_.group[j].professor);
	  strcpy(out[k].hour,text_.group[j].date[0].hour);
	  if(text_.Unit()>='3'){
	   k++;
	   strcpy(out[k].name,text_.name);
	   strcpy(out[k].day,text_.group[j].date[1].day);
	   out[k].group[0]=text_.group[j].number[0];
	   out[k].group[1]=0;
	   strcpy(out[k].professor,text_.group[j].professor);
	   strcpy(out[k].hour,text_.group[j].date[1].hour);
	  }
	}
       else {Message(4);strcpy(out[k].name,text_.name);}
    }
   else {Message(3);strcpy(out[k].name,"NONE");};
  }
 for(i=0;i<k;i++){
    Correct(out[i].name,'c');
    Correct(out[i].day,'c');
    Correct(out[i].hour,'c');
    Correct(out[i].professor,'c');
 }
 Sort(out,k);
 for(i=0;i<k;i++)
    out[i].Print(3+i);
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Student ::Show()
{
 textattr(0x1f);
 gotoxy(16,3);  Correct(name,'c');                    cprintf("%s",name);
 gotoxy(16,4);  Correct(surname,'c');                 cprintf("%s",surname);
 gotoxy(16,5);  Correct(number,'n');                  cprintf("%s",number);
 gotoxy(16,6);  Correct(age,'n');                     cprintf("%s",age);
 gotoxy(16,7);  if(!strcmp(sex,"m")||!strcmp(sex,"M"))cprintf("Male");
		else cprintf("Female");
 gotoxy(16,8);  Correct(phonenumber,'n'); cprintf("%s",phonenumber);
 gotoxy(16,9);  Correct(city,'c');        cprintf("%s",city);
 gotoxy(16,10); Correct(department,'c');  cprintf("%s",department);
 gotoxy(16,11); Correct(rank,'c');        cprintf("%s",rank);
 gotoxy(16,12); Correct(course,'n');      cprintf("%s",course);
 gotoxy(16,13); Correct(average,'n');     cprintf("%s",average);
 gotoxy(16,14); Correct(passed_units,'n');cprintf("%s",passed_units);
 gotoxy(17,15);                           cprintf("%s",num);
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Student::Texts()
{
 Clean();
 textattr(0x1b);
 gotoxy(3,3);  cprintf("Name        :");
 gotoxy(3,4);  cprintf("Surname     :");
 gotoxy(3,5);  cprintf("St-Number   :");
 gotoxy(3,6);  cprintf("Age         :");
 gotoxy(3,7);  cprintf("Sex(M-F)    :");
 gotoxy(3,8);  cprintf("Phone       :");
 gotoxy(3,9);  cprintf("City        :");
 gotoxy(3,10); cprintf("Department  :");
 gotoxy(3,11); cprintf("Rank        :");
 gotoxy(3,12); cprintf("Course      :");
 gotoxy(3,13); cprintf("Average     :");
 gotoxy(3,14); cprintf("Passed Units:");
 gotoxy(3,15); cprintf("NumberOfTexts:");
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Text::Get(void)
{
 int n;
 int j=0;
 char c[2];
 textattr(0x1f);
 gotoxy(18,3);cprintf("¯");cscanf("%s",name);
 gotoxy(18,4);cprintf("¯");cscanf("%s",code);
 gotoxy(18,5);cprintf("¯");cscanf("%s",c);
 unit=c[0];
 gotoxy(18,6);cprintf("¯");cscanf("%d",&n);
 gotoxy(45,3);cprintf("%s",name);
 num[0]=n+'0';
 num[1]=0;
 for(int i=0;i<n;i++){
    gotoxy(57,3+i+j);cprintf("%d",i+1);
   group[i].number[0]=i+1+'0';
   group[i].number[1]=0;
   gotoxy(32,3+i+j);cprintf("¯");cscanf("%s",group[i].date[0].day);
   group[i].date[0].day[0]=toupper(group[i].date[0].day[0]);
   gotoxy(62,3+i+j);cprintf("¯");cscanf("%s",group[i].professor);
   gotoxy(74,3+i+j);cprintf("¯");cscanf("%s",group[i].date[0].hour);
   if(unit>='3'){
     gotoxy(57,3+i+j+1);cprintf("%d",i+1);
     gotoxy(62,3+i+j+1);cprintf("¯");cprintf("%s",group[i].professor);
     gotoxy(32,3+i+j+1);cprintf("¯");cscanf("%s",group[i].date[1].day);
     group[i].date[1].day[0]=toupper(group[i].date[1].day[0]);
     gotoxy(74,3+i+j+1);cprintf("¯");cscanf("%s",group[i].date[1].hour);
    j++;
   }
 }
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Text::Insert(void)
{
 fstream out;
 out.open("Texts.txt",ios::app);
 if(!out) Message(2);
 out.write((char*)this,sizeof(Text));
 out.put('\n');
 out.close();
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Text::Normal(void)
{
 char temp[LEN];
 StrToLen(name,temp,LEN,'c');
 strcpy(name,temp);
 StrToLen(code,temp,LEN,'n');
 strcpy(code,temp);
 for(int i=0;i<N_OF_G;i++)
   group[i].Normal();
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
void Text::Texts(void)
{
 textattr(0x1b);
 gotoxy(3,3);cprintf("Name          :");
 gotoxy(3,4);cprintf("Code          :");
 gotoxy(3,5);cprintf("Unit          :");
 gotoxy(3,6);cprintf("NumberOfGroups:");
 }
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-