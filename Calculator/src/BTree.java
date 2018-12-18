public interface BTree {
	 /** 
     * 添加左子树 
     * @param lChild 左子树 
     */  
    public void addLeftTree(BTree lChild);  
    /** 
     * 添加右子树 
     * @param rchild 右子树 
     */  
    public void addRightTree(BTree rchild) ;  
    /** 
     * 置空树 
     */  
    public void clearTree();  
    /** 
     * 求树的深度 
     * @return 树的深度 
     */  
    public int dept();  
    /** 
     * 求左孩子 结点 
     * @return 
     */  
    public BTree getLeftChild();  
      
    /** 
     * 求右孩子结点 
     * @return 
     */  
    public BTree getRightChild();  
    /** 
     * 获得根结点的数据 
     * @return 
     */  
    public String getRootData();  
    /** 
     * 是否有左子树 
     * @return 
     */  
    public boolean hasLeftTree();  
    /** 
     * 是否有右子树 
     * @return 
     */  
    public boolean hasRightTree();  
    /** 
     * 判断是否为空树 
     * @return 如果为空，返回true,否则返回false 
     */  
    public boolean isEmpty();  
    /** 
     * 判断是否为叶子结点 
     * @return 
     */  
    public boolean isLeaf();  
    /** 
     * 删除左子树 
     */  
    public void removeLeftChild();  
    /** 
     * 删除右子树 
     */  
    public void removeRightChild();  
    /** 
     * 获得树根 
     * @return 树的根 
     */  
    public BTree root();  
    /** 
     * 设置根结点的数据 
     */  
    public void setRootData(String data);  
    /** 
     * 求结点数 
     * @return 结点的个数  
     */  
    public int size();  

}
