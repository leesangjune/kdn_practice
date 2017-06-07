package com.kdn.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kdn.model.biz.BoardService;
import com.kdn.model.domain.Board;
import com.kdn.model.domain.PageBean;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@RequestMapping(value="insertBoardForm.do", method=RequestMethod.GET)
	public String insertBoardForm(Model model){
		model.addAttribute("content", "board/insertBoard.jsp");
		return "index";
	}
	
	@RequestMapping(value="insertBoard.do", method=RequestMethod.POST)
	public String insertBoard(Board board, HttpServletRequest request){
		String dir = request.getRealPath("upload/");
		boardService.add(board, dir);
		return "redirect:listBoard.do";
	}
	
	@RequestMapping(value="listBoard.do", method=RequestMethod.GET)
	public String listBoard(PageBean bean, Model model){
		List<Board> list = boardService.searchAll(bean);
		model.addAttribute("list", list);
		model.addAttribute("content", "board/listBoard.jsp");
		return "index";
	}
	@RequestMapping(value="searchBoard.do", method=RequestMethod.GET)
	public String searchBoard(int no, Model model){
		model.addAttribute("board", boardService.search(no));
		model.addAttribute("content", "board/searchBoard.jsp");
		return "index";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Autowired
//	private BoardService  service;
//
//	@RequestMapping("listBoard.do")
//	public String boardList(PageBean pageBean, Model model){
//		
//		model.addAttribute("list", service.searchAll(pageBean));
//		model.addAttribute("content", "board/listBoard.jsp");
//		return "index";
//	}
//	
//	@RequestMapping("fileUploadForm.do")
//	public String fileUploadForm(){
//		return "board/fileUploadForm";
//	}
//	
//	@RequestMapping(value="boardInsert.do", method=RequestMethod.POST)
//	public String boardInsert(Board board, HttpServletRequest request){
//		String dir = request.getRealPath("/upload/");
//		return "redirect:boardList.do";
//	}
//	
//	
//	@RequestMapping("boardDetail.do")
//	public String boardDetail(String no, Model model){
//		model.addAttribute("board", service.search(Integer.parseInt(no)));
//		return "board/boardDetail";
//	}
//	
//	
//	@RequestMapping("boardUpdate.do")
//	public String boardUpdate(Board board, Model model, String query){
////		service.boardUpdate(board);
//		System.out.println("query..."+query);
//		return "redirect:boardList.do?"+query;
//	}
//	
//	@RequestMapping(value="boardDelete.do",method=RequestMethod.GET)
//	public String boardDelete(String no, String query, Model model, HttpServletRequest  request){
//		String dir = request.getRealPath("/upload/");
////		service.boardDelete(no, dir);
//		return "redirect:boardList.do?"+query;
//	}
	
}




