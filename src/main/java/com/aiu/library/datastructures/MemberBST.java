package com.aiu.library.datastructures;

import java.util.ArrayList;
import java.util.List;

import com.aiu.library.model.Book;
import com.aiu.library.model.Member;

public class MemberBST {
    class Node {
        Member data;
        Node left, right;

        Node(Member data) {
            this.data = data;
        }
    }

    private Node root;
    public void insert(Member m) {
        root = insertRec(root, m);
    }

    private Node insertRec(Node root, Member m) {

        if (root == null) {
            root = new Node(m);
            return root;
        }

        if (m.getMemberId() < root.data.getMemberId()) {
            root.left = insertRec(root.left, m);
        } else if (m.getMemberId() > root.data.getMemberId()) {
            root.right = insertRec(root.right, m);
        }

        return root;
    }

    public Member search(int id) {
        Node node = searchRec(root, id);
        return (node != null) ? node.data : null;
    }

    private Node searchRec(Node root, int id) {
        if (root == null || root.data.getMemberId() == id) {
            return root;
        }

        if (id < root.data.getMemberId()) {
            return searchRec(root.left, id);
        }

        return searchRec(root.right, id);
    }
    public List<Member> listMembers() {
        List<Member> list = new ArrayList<>();
        inorderRec(root, list);
        return list;
    }

    private void inorderRec(Node root, List<Member> list) {
        if (root != null) {
            inorderRec(root.left, list);
            list.add(root.data);
            inorderRec(root.right, list);
        }
    }

    public Book getName() {

        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }
}
