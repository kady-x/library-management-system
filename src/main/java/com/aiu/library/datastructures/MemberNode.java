package com.aiu.library.datastructures;

import com.aiu.library.model.Member;
import java.util.List;

public class MemberNode {
    Member data;
    MemberNode left, right;

    MemberNode(Member data) {
        this.data = data;
    }

    public void insert(Member member) {
        if (member.getMemberId() < this.data.getMemberId()) {
            if (this.left == null)
                this.left = new MemberNode(member);
            else
                this.left.insert(member);
        } else {
            if (this.right == null)
                this.right = new MemberNode(member);
            else
                this.right.insert(member);
        }
    }

    public Member searchById(Integer id) {
        if (id.equals(this.data.getMemberId())) return this.data;

        if (id < this.data.getMemberId()) {
            return (left == null) ? null : left.searchById(id);
        } else {
            return (right == null) ? null : right.searchById(id);
        }
    }

    public Member searchByName(String name) {
        if (this.data.getName().equalsIgnoreCase(name)) return this.data;

        Member result = null;
        
        if (this.left != null) result = left.searchByName(name);
        if (result != null) return result;

        if (this.right != null) result = right.searchByName(name);
        return result;
    }
    
    public void getAllMembers(List<Member> list) {
        if (this.left != null)
            this.left.getAllMembers(list);

        list.add(this.data);

        if (this.right != null)
            this.right.getAllMembers(list);
    }
}
