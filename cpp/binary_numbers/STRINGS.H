#include<iostream.h>
#include<string.h>
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
class String{
    friend int      operator==(const String&,const String&);
    friend int      operator!=(const String&,const String&);
    friend int      operator< (const String&,const String&);
    friend int      operator<=(const String&,const String&);
    friend int      operator> (const String&,const String&);
    friend int      operator>=(const String&,const String&);
    friend String   operator+ (const String&,const String&);
    friend String   operator<<(String&,int n);
    friend String   operator>>(String&,int n);
    friend String  operator& (String,String);
    friend String  operator| (String,String);
    friend String   operator^ (String&,String&);
    friend String   operator~ (const String&);
    friend ostream& operator<<(ostream&,const String&);
    friend istream& operator>>(istream&,String&);
 public:
    //String(unsigned=0);
    String(char,unsigned=0);
    String(const char*);
    String(const String&);
    ~String();
    String& operator=  (const String&);
    String& operator+= (const String&);
    String& operator<<=(int);
    String& operator>>=(int);
    String& operator&= (const String&);
    String& operator|= (const String&);
    String& operator^= (const String&);
    char&   operator[] (unsigned)const;
	    operator const char*() const;
	int operator! ();
    unsigned lenght() const;
 private:
    unsigned len;
    char* buf;
};
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
/*String::String(unsigned n):len(n)
{
 buf=new char [len+1];
 for(int i=0;i<len;i++)
    buf[i]=' ';
 buf[len]=0;
} */
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String::String(char c,unsigned n):len(n)
{
 buf=new char [len+1];
 for(int i=0;i<len;i++)
    buf[i]=c;
 buf[len]=0;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String::String(const char* s)
{
 len=strlen(s);
 buf=new char [len+1];
 for(int i=0;i<len;i++)
    buf[i]=s[i];
 buf[len]=0;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String::String(const String& s) :len(s.len)
{
 buf=new char [len+1];
 for(int i=0;i<s.len;i++)
    buf[i]=s.buf[i];
 buf[i]=0;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String::~String()
{
 delete [] buf;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String& String::operator=(const String& s)
{
 if(&s==this) return *this;
 len=s.len;
 delete [] buf;
 buf=new char[s.len+1];
 strcpy(buf,s.buf);
 return *this;
} /*
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
*/String operator+(const String& s1,const String& s2)
{
 String s(s1.len+s2.len+1);
 strcpy(s.buf,s1.buf);
 strcat(s.buf,s2.buf);
 return s;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String& String::operator+=(const String& s)
{
 len+=s.len;
 char* tempbuf=new char [len+1];
 strcpy(tempbuf,buf);
 strcat(tempbuf,s.buf);
 delete [] buf;
 buf=tempbuf;
 return *this;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String:: operator  const char*() const
{
 return buf;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
int operator ==(const String& s1,const String & s2)
{
 return (strcmp(s1.buf,s2.buf)==0);
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
int operator!=(const String& s1,const String & s2)
{
 return strcmp(s1.buf,s2.buf)!=0;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
int operator<(const String& s1,const String & s2)
{
 return strcmp(s1.buf,s2.buf)<0;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
int operator<=(const String& s1,const String & s2)
{
 return strcmp(s1.buf,s2.buf)<=0;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
int operator>(const String& s1,const String & s2)
{
 return strcmp(s1.buf,s2.buf)>0;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
int operator>=(const String& s1,const String & s2)
{
 return strcmp(s1.buf,s2.buf)>=0;
}
int String::operator! ()
{
 return  (*buf==NULL) ;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
istream& operator>>(istream& istr,String& s)
{
 char buffer[256];
 istr.get(s.buf,s.len+1);
 return istr;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
ostream& operator<<(ostream& ostr,const String& s)
{
  return ostr << s.buf;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
char& String::operator[] (unsigned i)const
{
 return buf[i];
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
unsigned String::lenght() const
{
 return strlen(buf);
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String operator&(String s1,String s2)
{
 unsigned minlen=((s1.len<s2.len)?s1.len:s2.len);
 String s('0',minlen);
 for (int i=1;i<=minlen;i++)
   if(s1.buf[s1.len-i]=='1'&&s2.buf[s2.len-i]=='1')s.buf[minlen-i]='1';
 return s;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String operator|(String s1,String s2)
{
  unsigned minlen=((s1.len<s2.len)?s1.len:s2.len);
  unsigned maxlen=((s1.len>s2.len)?s1.len:s2.len);
  String s('0',maxlen);
  for(int i=1;i<=minlen;i++)
    if(s1.buf[s1.len-i]=='1'||s2.buf[s2.len-i]=='1' )
    s.buf[maxlen-i]='1';
  for(i=minlen+1;i<=maxlen;i++)
    if(maxlen==s1.len)s.buf[maxlen-i]=s1.buf[s1.len-i];
    else s.buf[maxlen-i]=s2.buf[s2.len-i];
  return s;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String operator^(String&s1,String&s2)
{
 unsigned minlen=((s1.len<s2.len)?s1.len:s2.len);
 unsigned maxlen=((s1.len>s2.len)?s1.len:s2.len);
 String s('0',maxlen );
 for(int i=1;i<minlen;i++)
   if(s1.buf[s1.len-i]!=s2.buf[s2.len-i]) s.buf[maxlen-i]='1';
 for(i=minlen+1;i<=maxlen;i++)
    if(maxlen==s1.len){
      if(s1.buf[maxlen-i]=='1') s.buf[maxlen-i]='1';
    }
    else if(s2.buf[maxlen-i]=='1') s.buf[maxlen-i]='1';
 return s;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String& String::operator&= (const String& s)
{
 unsigned minlen=((s.len<len)?s.len:len);
 for(int i=1;i<=minlen;i++)
 if(buf[i]=='1' &&s.buf[i]=='1')buf[i]='1';
 else buf[i]='0';
 for(i=minlen;i<len;i++)
  buf[i]='0';
 return *this;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String& String::operator|= (const String& s)
{
 unsigned minlen=((len<s.len)?len:s.len);
 unsigned maxlen=((len>s.len)?len:s.len);
  String t('0',maxlen);
  for(int i=1;i<=minlen;i++)
    if(s.buf[s.len-i]=='1'||buf[len-i]=='1' )
    t.buf[maxlen-i]='1';
  for(i=minlen+1;i<=maxlen;i++)
    if(maxlen==s.len)t.buf[maxlen-i]=s.buf[s.len-i];
    else t.buf[maxlen-i]=buf[len-i];
    *this=s;
 return *this;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String& String::operator^= (const String&s)
{
 unsigned minlen=((s.len<len)?s.len:len);
 unsigned maxlen=((s.len>len)?s.len:len);
 String t('0',maxlen );
 for(int i=1;i<minlen;i++)
   if(s.buf[s.len-i]!=buf[len-i]) t.buf[maxlen-i]='1';
 for(i=minlen+1;i<=maxlen;i++)
    if(maxlen==s.len){
      if(s.buf[maxlen-i]=='1') t.buf[maxlen-i]='1';
    }
    else if(buf[maxlen-i]=='1') t.buf[maxlen-i]='1';
   *this=t;
   return *this;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String operator~(const String& s)
{
 String not('0',s.len);
 for(int i=0;i<s.len;i++)
  if (s.buf[i]=='1' ) not.buf[i]='0';
  else not.buf[i]='1';
 return not;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String operator<<(String& s,int n)
{
  String p('0',s.len);
  for(int i=0;i<n;i++)
    p[s.len-i-1]='0';
  for(;i<s.len;i++)
    p[s.len-i-1]=s[s.len-i+n-1];
  return p;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String operator>>(String&s,int n)
{
 String p('0',s.len);
 for(int i=0;i<n;i++)
   p[i]='0';
 for(;i<s.len;i++)
   p[i]=s[i-n];
 return p;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String& String ::operator<<=(int n)
{
 String p('0',len);
  for(int i=0;i<n;i++)
    p[len-i-1]='0';
  for(;i<len;i++)
    p[len-i-1]=buf[len-i+n-1];
    *this=p;
 return *this;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
String& String ::operator>>=(int n)
{
 String p('0',len);
 for(int i=0;i<n;i++)
   p[i]='0';
 for(;i<len;i++)
   p[i]=buf[i-n];
 *this= p;
 return *this;
}
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=