import java.util.NoSuchElementException;

public class LazySearchTree<E extends Comparable<? super E>> implements
      Cloneable
{

   protected class LazySTNode<E extends Comparable<? super E>>
   {
      // use public access so the tree or other classes can access members
      public LazySTNode<E> lftChild, rtChild;
      public E data;
      public LazySTNode<E> myRoot; // needed to test for certain error
      public boolean deleted;

      public LazySTNode(E d, LazySTNode<E> lft, LazySTNode<E> rt, boolean del)
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
      public int getHeight()
      {
         return 0;
      }

      boolean setHeight(int height)
      {
         return true;
      }
   }

   protected static boolean DEBUG = false;
   protected int mSize;
   protected LazySTNode<E> mRoot;
   protected int mSizeHard;

   public LazySearchTree()
   {
      clear();
   }

   public boolean empty()
   {
      return (mSize == 0);
   }

   public int size()
   {
      return mSize;
   }

   public void clear()
   {
      mSize = 0;
      mSizeHard = 0;
      mRoot = null;
   }

   public int showHeight()
   {
      return findHeight(mRoot, -1);
   }

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

   public E find(E x)
   {
      LazySTNode<E> resultNode;
      resultNode = find(mRoot, x);
      if (resultNode == null)
         throw new NoSuchElementException();

      return resultNode.data;
   }

   public boolean contains(E x)
   {
      return find(mRoot, x) != null;
   }

   public boolean insert(E x)
   {
      int oldSize = mSize;
      mRoot = insert(mRoot, x);
      return (mSize != oldSize);
   }

   public boolean remove(E x)
   {
      int oldSize = mSize;
      remove(mRoot, x);
      return (mSize != oldSize);
   }

   public boolean collectGarbage()
   {
      this.collectGarbage(mRoot);
      return true;
   }

   public boolean removeHard(E x)
   {
      int oldSize = mSize;
      remove(mRoot, x);
      return (mSize != oldSize);
   }

   public <F extends Traverser<? super E>> void traverse(F func)
   {
      traverse(func, mRoot);
   }

   public Object clone() throws CloneNotSupportedException
   {
      LazySearchTree<E> newObject = (LazySearchTree<E>) super.clone();
      newObject.clear(); // can't point to other's data

      newObject.mRoot = cloneSubtree(mRoot);
      newObject.mSize = mSize;

      return newObject;
   }

   // private helper methods ----------------------------------------
   protected LazySTNode<E> findMin(LazySTNode<E> root)
   {
      if (root == null)
         return null;
      if (root.lftChild == null)
         return root;
      return findMin(root.lftChild);
   }

   protected LazySTNode<E> findMax(LazySTNode<E> root)
   {
      if (root == null)
         return null;
      if (root.rtChild == null)
         return root;
      return findMax(root.rtChild);
   }

   protected LazySTNode<E> insert(LazySTNode<E> root, E x)
   {
      int compareResult; // avoid multiple calls to compareTo()

      if (root == null)
      {
         mSize++;
         mSizeHard++;
         return new LazySTNode<E>(x, null, null, false);
      }

      compareResult = x.compareTo(root.data);
      if (compareResult < 0)
         root.lftChild = insert(root.lftChild, x);
      else if (compareResult > 0)
         root.rtChild = insert(root.rtChild, x);

      return root;
   }

   protected boolean remove(LazySTNode<E> root, E x)
   {
      if (root == null)
         return false;
      if (find(x) != null)
      {
         find(root, x).deleted = true;
         mSize--;
         return true;
      } else
      {
         return false;
      }
   }

   protected LazySTNode<E> removeHard(LazySTNode<E> root)
   {
      
      if (root == null)
         return null;
      

      if (root.lftChild != null && root.lftChild.deleted) // 1
      {
         if (DEBUG)
         {
            System.out.println("removeHard Step 1");
            printNode(root);
            printNode(root.lftChild);
            printNode(root.rtChild);
            System.out.println("mSizeHard: " + mSizeHard );
         }

         root.lftChild = removeHard(root.lftChild);
         mSizeHard--;
      } else if (root.rtChild != null && root.rtChild.deleted) // 2
      {
         if (DEBUG)
         {
            System.out.println("removeHard Step 2");
            printNode(root);
            printNode(root.lftChild);
            printNode(root.rtChild);
            System.out.println("mSizeHard: " + mSizeHard );
         }
         root.rtChild = removeHard(root.rtChild);
         mSizeHard--;
      }
      // found the node
      else if (root.lftChild != null && root.rtChild != null && root.deleted) // 3
      {
         if (DEBUG)
         {
            System.out.println("removeHard Step 3.0");
            printNode(root);
            printNode(root.lftChild);
            printNode(root.rtChild);
            System.out.println("mSizeHard: " + mSizeHard );
         }
//         root.data = findMin(root.rtChild).data;
//         if (DEBUG)
//         {
//            System.out.println("removeHard Step 3.1");
//            printNode(root);
//            printNode(root.lftChild);
//            printNode(root.rtChild);
//            System.out.println("mSizeHard: " + mSizeHard );
//         }
         
         root.data = removeHard(findMin(root.rtChild)).data;
         mSizeHard--;
         root.deleted = false;
      } else
      {
         root = (root.lftChild != null) ? root.lftChild : root.rtChild;// 4
         System.out.println("444444444444444444444444444444");
         printNode(root);
         System.out.println("mSizeHard: " + mSizeHard );
        // mSizeHard--;
      }
      System.out.println("returning node " );
      System.out.println("mSizeHard: " + mSizeHard );
      return root;
   }

   protected void collectGarbage(LazySTNode<E> mRoot)
   {
      removeHard(mRoot);
      removeHard(mRoot.lftChild);
      removeHard(mRoot.rtChild);
     
   }

   protected <F extends Traverser<? super E>> void traverse(F func,
         LazySTNode<E> treeNode)
   {
      if (treeNode == null)
         return;

      traverse(func, treeNode.lftChild);
      if (!treeNode.deleted)
         func.visit(treeNode.data);
      traverse(func, treeNode.rtChild);
   }

   protected LazySTNode<E> find(LazySTNode<E> root, E x)
   {
      int compareResult; // avoid multiple calls to compareTo()

      if (root == null)
         return null;

      compareResult = x.compareTo(root.data);
      if (compareResult < 0)
         return find(root.lftChild, x);
      if (compareResult > 0)
         return find(root.rtChild, x);
      if (!root.deleted)
         return root; // found
      return null;
   }

   protected LazySTNode<E> cloneSubtree(LazySTNode<E> root)
   {
      LazySTNode<E> newNode;
      if (root == null)
         return null;

      // does not set myRoot which must be done by caller
      newNode = new LazySTNode<E>(root.data, cloneSubtree(root.lftChild),
            cloneSubtree(root.rtChild), root.deleted);
      return newNode;
   }

   protected int findHeight(LazySTNode<E> treeNode, int height)
   {
      int leftHeight, rightHeight;
      if (treeNode == null)
         return height;
      height++;
      leftHeight = findHeight(treeNode.lftChild, height);
      rightHeight = findHeight(treeNode.rtChild, height);
      return (leftHeight > rightHeight) ? leftHeight : rightHeight;
   }

   protected void printNode(LazySTNode<E> node)
   {  
      if( node != null)
      {
      System.out.println("node.data: " + node.data);
      if (node.rtChild != null)
         System.out.println("\tnode.rtChild: " + node.rtChild.data);
      if (node.lftChild != null)
         System.out.println("\tnode.lftChild: " + node.lftChild.data);    
      System.out.println("\tnode.deleted: " + node.deleted);
      System.out.println();
      } else {
         System.out.println("this node is null.");
      }
   }

   class DeleteObject<E> implements Traverser<E>
   {
      public void visit(E x)
      {

      }

   }

}
