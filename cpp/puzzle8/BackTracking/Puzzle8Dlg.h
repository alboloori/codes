// Puzzle8Dlg.h : header file
//

#if !defined(AFX_PUZZLE8DLG_H__81B56090_93D4_4406_9E6A_0787C12E0D4C__INCLUDED_)
#define AFX_PUZZLE8DLG_H__81B56090_93D4_4406_9E6A_0787C12E0D4C__INCLUDED_
#include "Node.h"	// Added by ClassView
#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
#include <queue>
#include "DepthDialog.h"
using namespace std;
/////////////////////////////////////////////////////////////////////////////
// CPuzzle8Dlg dialog

class CPuzzle8Dlg : public CDialog
{
// Construction
public:
	CDepthDialog m_DepthDlg;
	Node* m_pPuzzle;
	Node* m_pStart;
	Node* m_pGoal;
	CString m_strPath;
	CString m_strPathOut;
	CString m_strPromptDepth;
	CString m_strPrompt;
	char m_strDepth[15];
	int depth_limit;
	int m_nNextMove;
	bool m_bIsGoal;
	void FindGoal(Node node,Side lastmove) ;
	bool IsGoal(Node node);
	CPuzzle8Dlg(CWnd* pParent = NULL);	// standard constructor
// Dialog Data
	//{{AFX_DATA(CPuzzle8Dlg)
	enum { IDD = IDD_PUZZLE8_DIALOG };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CPuzzle8Dlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	HICON m_hIcon;
	// Generated message map functions
	//{{AFX_MSG(CPuzzle8Dlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg void OnOptionsExit();
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
	afx_msg void OnOptionsNewgame();
	afx_msg void OnOptionsSolve();
	afx_msg void OnHelpAbout();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnOptionsDepth();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_PUZZLE8DLG_H__81B56090_93D4_4406_9E6A_0787C12E0D4C__INCLUDED_)
