; CLW file contains information for the MFC ClassWizard

[General Info]
Version=1
LastClass=CPuzzle8Dlg
LastTemplate=CDialog
NewFileInclude1=#include "stdafx.h"
NewFileInclude2=#include "Puzzle8.h"

ClassCount=5
Class1=CPuzzle8App
Class2=CPuzzle8Dlg
Class3=CAboutDlg

ResourceCount=8
Resource1=IDD_ABOUTBOX
Resource2=IDR_MAINFRAME
Resource3=IDD_PUZZLE8_DIALOG
Resource4=IDD_PUZZLE8_DIALOG (English (U.S.))
Resource5=IDD_DIALOG_SELECT_GREEDY
Class4=n
Resource6=IDR_MENU_OPTIONS
Class5=CSHDIalog
Resource7=IDD_ABOUTBOX (English (U.S.))
Resource8=IDR_ACCELERATOR1

[CLS:CPuzzle8App]
Type=0
HeaderFile=Puzzle8.h
ImplementationFile=Puzzle8.cpp
Filter=N

[CLS:CPuzzle8Dlg]
Type=0
HeaderFile=Puzzle8Dlg.h
ImplementationFile=Puzzle8Dlg.cpp
Filter=D
BaseClass=CDialog
VirtualFilter=dWC
LastObject=ID_OPTIONS_SELECT_GREEDYMETHOD

[CLS:CAboutDlg]
Type=0
HeaderFile=Puzzle8Dlg.h
ImplementationFile=Puzzle8Dlg.cpp
Filter=D

[DLG:IDD_ABOUTBOX]
Type=1
ControlCount=4
Control1=IDC_STATIC,static,1342177283
Control2=IDC_STATIC,static,1342308352
Control3=IDC_STATIC,static,1342308352
Control4=IDOK,button,1342373889
Class=CAboutDlg


[DLG:IDD_PUZZLE8_DIALOG]
Type=1
ControlCount=3
Control1=IDOK,button,1342242817
Control2=IDCANCEL,button,1342242816
Control3=IDC_STATIC,static,1342308352
Class=CPuzzle8Dlg

[DLG:IDD_PUZZLE8_DIALOG (English (U.S.))]
Type=1
Class=CPuzzle8Dlg
ControlCount=0

[DLG:IDD_ABOUTBOX (English (U.S.))]
Type=1
Class=CAboutDlg
ControlCount=7
Control1=IDC_STATIC,static,1342177283
Control2=IDC_STATIC,static,1342308480
Control3=IDC_STATIC,static,1342308352
Control4=IDOK,button,1342373889
Control5=IDC_STATIC,static,1342308352
Control6=IDC_STATIC,static,1342308352
Control7=IDC_STATIC,static,1342308352

[MNU:IDR_MENU_OPTIONS]
Type=1
Class=?
Command1=ID_OPTIONS_NEWGAME
Command2=ID_OPTIONS_SELECT_GREEDYMETHOD
Command3=ID_OPTIONS_SOLVE
Command4=ID_OPTIONS_EXIT
Command5=ID_HELP_ABOUT
CommandCount=5

[CLS:n]
Type=0
HeaderFile=n.h
ImplementationFile=n.cpp
BaseClass=CAnimateCtrl
Filter=W

[CLS:CSHDIalog]
Type=0
HeaderFile=SHDIalog.h
ImplementationFile=SHDIalog.cpp
BaseClass=CDialog
Filter=D
LastObject=ID_OPTIONS_NEWGAME
VirtualFilter=dWC

[ACL:IDR_ACCELERATOR1]
Type=1
Class=?
Command1=ID_OPTIONS_EXIT
Command2=ID_OPTIONS_SELECT_HEURISTIC
Command3=ID_OPTIONS_NEWGAME
Command4=ID_OPTIONS_SOLVE
CommandCount=4

[DLG:IDD_DIALOG_SELECT_GREEDY]
Type=1
Class=CSHDIalog
ControlCount=3
Control1=IDOK,button,1342242817
Control2=IDC_COMBO_GREEDY,combobox,1344340226
Control3=IDC_STATIC,static,1342308352

