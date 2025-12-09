package com.aiu.library.datastructures;

import com.aiu.library.model.Member;
import java.util.ArrayList;
import java.util.List;

import com.aiu.library.model.Member;

public class MemberBST {
    private MemberNode root;

    public MemberBST(){
        this.root = null;
    }

    public MemberNode getRoot(){
        return root;
    }

    public void addMember(Member member) {
        if (root == null) {
            root = new MemberNode(member);
        } else {
            root.insert(member);
        }
    }

    public void updateMemberInfo(Member updatedMember) {
        delete(updatedMember.getMemberId());
        addMember(updatedMember);
    }

    public void delete(Integer id) {
        root = deleteRec(root, id);
    }

    private MemberNode deleteRec(MemberNode node, Integer id) {
        if (node == null) return null;

        if (id < node.data.getMemberId()) {
            node.left = deleteRec(node.left, id);
        } else if (id > node.data.getMemberId()) {
            node.right = deleteRec(node.right, id);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            MemberNode min = findMin(node.right);
            node.data = min.data;
            node.right = deleteRec(node.right, min.data.getMemberId());
        }

        return node;
    }
    
    private MemberNode findMin(MemberNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    public Member searchByID(Integer id) {
        if (root == null) return null;
        return root.searchById(id);
    }

    public Member searchByName(String name) {
        if (root == null) return null;
        return root.searchByName(name);
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        if (root != null)
            root.getAllMembers(members);
        return members;
    }

    public void inOrderTraversal(MemberNode node, List<Member> members) {
        if (node != null) {
            inOrderTraversal(node.left, members);
            members.add(node.data);
            inOrderTraversal(node.right, members);
        }
    }

    public Integer findMaxId(MemberNode node) {
        if (node == null) return null;
        while (node.right != null) {
            node = node.right;
        }
        return node.data.getMemberId();
    }

    public Member getName() {
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }
}
