public class LinkBTree implements BTree{
	private String data;  
    private BTree lChild;  
    private BTree rChild;  
      
    public LinkBTree() {  
        this.clearTree();  
    }  
    public LinkBTree(String data) {  
        this.data = data;  
        this.lChild = null;  
        this.rChild = null;  
    }  
    @Override  
    public void addLeftTree(BTree lChild) {  
        this.lChild = lChild;  
    }  
  
    @Override  
    public void addRightTree(BTree rChild) {  
        this.rChild = rChild;  
    }  
  
    @Override  
    public void clearTree() {  
        this.data = null;  
        this.lChild = null;  
        this.rChild = null;  
    }  
  
    @Override  
    public int dept() {  
        return dept(this);  
    }  
      
    private int dept(BTree btree) {  
        if(btree.isEmpty()) {  
            return 0;  
        }else if(btree.isLeaf()) {  
            return 1;  
        } else {  
            if(btree.getLeftChild() == null) {  
                return dept(btree.getRightChild()) + 1;  
            } else if(btree.getRightChild() == null) {  
                return dept(btree.getLeftChild()) + 1;  
            } else {  
                return Math.max(dept(btree.getLeftChild()), dept(btree.getRightChild()))+1;  
            }  
        }  
    }  
  
    @Override  
    public BTree getLeftChild() {  
        return lChild;  
    }  
  
  
    @Override  
    public BTree getRightChild() {  
        return rChild;  
    }  
  
    @Override  
    public String getRootData() {  
        return data;  
    }  
  
    @Override  
    public boolean hasLeftTree() {  
        if(lChild != null)  
            return true;  
        return false;  
    }  
  
    @Override  
    public boolean hasRightTree() {  
        if(rChild != null)  
            return true;  
        return false;  
    }  
  
    @Override  
    public boolean isEmpty() {  
        if((lChild == null && rChild == null && data == null) || this == null) {  
            return true;  
        }  
        return false;  
    }  
      
    @Override  
    public boolean isLeaf() {  
        if(lChild == null && rChild == null) {  
            return true;  
        }  
        return false;  
    }  
  
    @Override  
    public void removeLeftChild() {  
        lChild = null;  
    }  
  
    @Override  
    public void removeRightChild() {  
        rChild = null;  
    }  
    @Override  
    public BTree root() {  
        return this;  
    }  
    @Override  
    public void setRootData(String data) {  
        this.data = data;  
    }  
    @Override  
    public int size() {  
        return size(this);  
    }  
    private int size(BTree btree) {  
        if(btree == null)   
            return 0;  
        else if(btree.isLeaf())   
            return 1;  
        else {  
            if(btree.getLeftChild() == null) {  
                return size(btree.getRightChild()) + 1;  
            } else if(btree.getRightChild() == null) {  
                return size(btree.getLeftChild()) + 1;  
            } else {  
                return size(btree.getLeftChild()) + size(btree.getRightChild()) + 1;  
            }  
        }   
    }  

}
