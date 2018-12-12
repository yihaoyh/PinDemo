package com.yihao.pinterestdemo.modules.user

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yihao.pinterestdemo.R
import com.yihao.pinterestdemo.dto.Board
import kotlinx.android.synthetic.main.fragment_boards.view.*

/**
 * Created by 易昊 on 2018/12/9.
 */
class BoardFragment: Fragment() {
    private var recyclerView: RecyclerView ?= null
    private var adapter: BoardAdapter = BoardAdapter()
    fun setBoards(boards: List<Board>) {
        adapter?.setBoards(boards)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_boards, null)
        recyclerView = view!!.findViewById(R.id.recyclerView_board)
        recyclerView?.adapter = this.adapter
        recyclerView?.layoutManager = GridLayoutManager(activity, 2)
        return view
    }

    class BoardAdapter: RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {
        private var boards: List<Board> ?= null

        fun setBoards(boards: List<Board>){
            this.boards = boards
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): BoardViewHolder {
            var view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_board, null)
            return BoardViewHolder(view)
        }

        override fun getItemCount(): Int {
            return if(null != boards) {
                boards!!.size
            } else {
                0
            }
        }

        override fun onBindViewHolder(viewHolder: BoardViewHolder, position: Int) {
            var board: Board? = boards?.get(position)
            viewHolder.name.text = board?.name
        }

        class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var name: TextView = itemView.findViewById(R.id.tv_name)
        }

    }

}