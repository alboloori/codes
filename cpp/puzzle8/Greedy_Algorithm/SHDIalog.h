#if !defined(AFX_SHDIALOG_H__0304B522_4410_40C6_9CE8_C7864061933F__INCLUDED_)
#define AFX_SHDIALOG_H__0304B522_4410_40C6_9CE8_C7864061933F__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// SHDIalog.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// CSHDIalog dialog

class CSHDIalog : public CDialog
{
// Construction
public:
	int m_iHeuristic;
	int m_iGreedy;
	CSHDIalog(CWnd* pParent = NULL);   // standard constructor

// Dialog Data
	//{{AFX_DATA(CSHDIalog)
	enum { IDD = IDD_DIALOG_SELECT_GREEDY };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA


// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CSHDIalog)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:

	// Generated message map functions
	//{{AFX_MSG(CSHDIalog)
	virtual void OnOK();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_SHDIALOG_H__0304B522_4410_40C6_9CE8_C7864061933F__INCLUDED_)
