
import java.util.NoSuchElementException;

public class LazySearchTree<E extends Comparable< ? super E > >
   implements Cloneable
{
   protected class LazySTNode<E extends Comparable< ? super E > >
   {
      // use public access so the tree or other classes can access members 
      public LazySTNode<E> lftChild, rtChild;
      public E data;
      public LazySTNode<E> myRoot;  // needed to test for certain error
      public boolean deleted;
    

      public LazySTNode( E d, LazySTNode<E> lft, LazySTNode<E> rt, boolean del)
      {
         lftChild = lft; 
         rtChild = rt;
         data = d;
         deleted = del;
      }
      
      public LazySTNode()
      {
         this(null, null, null, false);
        
      }
      
      // function stubs -- for use only with AVL Trees when we extend
      public int getHeight() { return 0; }
      boolean setHeight(int height) { return true; }
   }
   
   protected int mSize;
   protected LazySTNode<E> mRoot;
   protected int mSizeHard;
   
   public LazySearchTree() { clear(); }
   public boolean empty() { return (mSize == 0); }
   public int size() { return mSize; }
   public void clear() { mSize = 0; mSizeHard = 0; mRoot = null; }
   public int showHeight() { return findHeight(mRoot, -1); }
   
   public int sizeHard()
   {
      return mSizeHard;
   }
   
   public E findMin() 
   {
      if (mRoot == null)
         throw new NoSuchElementException();
      return findMin(mRoot).data;
   }
   
   public E findMax() 
   {
      if (mRoot == null)
         throw new NoSuchElementException();
      return findMax(mRoot).data;
   }
   
   public E find( E x )
   {
      LazySTNode<E> resultNode;
      resultNode = find(mRoot, x);
      if (resultNode == null)
         throw new NoSuchElementException();
      return resultNode.data;
   }
   public boolean contains(E x)  { return find(mRoot, x) != null; }
   
   public boolean insert( E x )
   {
      int oldSize = mSize;
      mRoot = insert(mRoot, x);   
      return (mSize != oldSize);
   }
   
   public boolean remove( E x )
   {
      int oldSize = mSize;
      remove(mRoot, x);
      return (mSize != oldSize);
   }
   
   public < F extends Traverser<? super E > > 
   void traverse(F func)
   {
      traverse(func, mRoot);
   }
   
   public Object clone() throws CloneNotSupportedException
   {
      LazySearchTree<E> newObject = (LazySearchTree<E>)super.clone();
      newObject.clear();  // can't point to other's data

      newObject.mRoot = cloneSubtree(mRoot);
      newObject.mSize = mSize;
      
      return newObject;
   }
   
   // private helper methods ----------------------------------------
   protected LazySTNode<E> findMin( LazySTNode<E> root ) 
   {
      if (root == null)
         return null;
      if (root.lftChild == null)
         return root;
      return findMin(root.lftChild);
   }
   
   protected LazySTNode<E> findMax( LazySTNode<E> root ) 
   {
      if (root == null)
         return null;
      if (root.rtChild == null)
         return root;
      return findMax(root.rtChild);
   }
   
   protected LazySTNode<E> insert( LazySTNode<E> root, E x )
   {
      int compareResult;  // avoid multiple calls to compareTo()
      
      if (root == null)
      {
         mSize++;
         mSizeHard++;
         return new LazySTNode<E>(x, null, null, false);
      }
      
      compareResult = x.compareTo(root.data); 
      if ( compareResult < 0 )
         root.lftChild = insert(root.lftChild, x);
      else if ( compareResult > 0 )
         root.rtChild = insert(root.rtChild, x);

      return root;
   }

   protected void remove( LazySTNode<E> root, E x  )
   {
      
      find(root, x).deleted = true;  
         mSize--;
      
 
   }
   
   protected <F extends Traverser<? super E>> 
   void traverse(F func, LazySTNode<E> treeNode)
   {
      if (treeNode == null)
         return;

      traverse(func, treeNode.lftChild);
      if(!treeNode.deleted)
         func.visit(treeNode.data);
      traverse(func, treeNode.rtChild);
   }
   
   protected LazySTNode<E> find( LazySTNode<E> root, E x )
   {
      int compareResult;  // avoid multiple calls to compareTo()

      if (root == null)
         return null;

      compareResult = x.compareTo(root.data); 
      if (compareResult < 0)
         return find(root.lftChild, x);
      if (compareResult > 0)
         return find(root.rtChild, x);
      if (!root.deleted)
         return root;   // found
      return null;
   }
   
   protected LazySTNode<E> cloneSubtree(LazySTNode<E> root)
   {
      LazySTNode<E> newNode;
      if (root == null)
         return null;

      // does not set myRoot which must be done by caller
      newNode = new LazySTNode<E>
      (
         root.data, 
         cloneSubtree(root.lftChild), 
         cloneSubtree(root.rtChild),
         root.deleted
      );
      return newNode;
   }
   
   protected int findHeight( LazySTNode<E> treeNode, int height ) 
   {
      int leftHeight, rightHeight;
      if (treeNode == null)
         return height;
      height++;
      leftHeight = findHeight(treeNode.lftChild, height);
      rightHeight = findHeight(treeNode.rtChild, height);
      return (leftHeight > rightHeight)? leftHeight : rightHeight;
   }
}


