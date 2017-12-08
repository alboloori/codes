// Puzzle8.h : main header file for the PUZZLE8 application
//

#if !defined(AFX_PUZZLE8_H__10E0480F_5850_4190_A2D3_9F6C84A42457__INCLUDED_)
#define AFX_PUZZLE8_H__10E0480F_5850_4190_A2D3_9F6C84A42457__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"		// main symbols

/////////////////////////////////////////////////////////////////////////////
// CPuzzle8App:
// See Puzzle8.cpp for the implementation of this class
//

class CPuzzle8App : public CWinApp
{
public:
	CPuzzle8App();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CPuzzle8App)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation

	//{{AFX_MSG(CPuzzle8App)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_PUZZLE8_H__10E0480F_5850_4190_A2D3_9F6C84A42457__INCLUDED_)
