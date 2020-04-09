import java.util.Scanner;

public class RedBlackTree {

    private final int RED = 0;
    private final int BLACK = 1;
    private final Node nil = new Node(-1); 
    
    int recolorCount = 0, rotateCount = 0;
    private class Node {
        int key = -1, color = BLACK;
        Node left = nil , right = nil , parent = nil;

        Node(int key) {
            this.key = key;
        } 
    }
    //private final Node nil = new Node(-1); 
    private Node root = nil;
    public void printTree(Node node) {
        if (node == nil) {
            return;
        }
        printTree(node.left);
        System.out.print(((node.color==RED)?"Color: Red ":"Color: Black ")+"Key: "+node.key+" Parent: "+node.parent.key+"\n");
        printTree(node.right);
        
    }
    private void insert(Node node) {
        int probe  = 0;
        Node temp = root;
        if (root == nil) {
            root = node;
            node.color = BLACK;
            recolorCount++;
            probe++;
            
           System.out.println("Probe: " +probe+ " Count of Recoloring after insertion : " +recolorCount );
            node.parent = nil;
        } else {
            node.color = RED;
            recolorCount++;
            probe++;
         //   System.out.println("Count of Recoloring after insertion : " +recolorCount );
            while (true) {
                if (node.key < temp.key) {
                    //probe++;
                    if (temp.left == nil) {
                        temp.left = node;
                        node.parent = temp;
                        probe++;
                        break;
                    } else {
                        temp = temp.left;
                        probe++;
                    }
                } else if (node.key >= temp.key) {
                   // probe++;
                    if (temp.right == nil) {
                        temp.right = node;
                        node.parent = temp;
                        probe++;
                        break;
                    } else {
                        temp = temp.right;
                        probe++;
                    }
                }
            }
           // System.out.println("Probe: " +probe+ " Count of Recoloring after insertion : " +recolorCount );
            int re = fixTree(node, recolorCount);
            System.out.println("Probe: "+probe+ " Count of Recoloring after insertion : " +re );
        }
    }
private int fixTree(Node node, int reColor) {
    //int rotate;
        while (node.parent.color == RED) {
            Node uncle = nil;
            if (node.parent == node.parent.parent.left) {
                uncle = node.parent.parent.right;

                if (uncle != nil && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    reColor++;
                    continue;
                } 
                if (node == node.parent.right) {
                    //Double rotation needed
                    node = node.parent;
                    rotateLeft(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                reColor++;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation 
                rotateRight(node.parent.parent);
            } else {
                uncle = node.parent.parent.left;
                 if (uncle != nil && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    reColor++;
                    continue;
                }
                if (node == node.parent.left) {
                    //Double rotation needed
                    node = node.parent;
                    //rotate = rotateRight(node);
                    rotateRight(node);
                   
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                reColor++;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation
                // rotate = rotateLeft(node.parent.parent);
                 rotateLeft(node.parent.parent);
            }
        }
        root.color = BLACK;
        reColor++;
        return reColor;
    }

     void rotateLeft(Node node) {
         rotateCount++;
         System.out.println("Rotation count : " + rotateCount);
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {//Need to rotate root
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
        //return rotateCount;
    }
    void rotateRight(Node node) {
        rotateCount++;
         System.out.println("Rotation count : " + rotateCount);
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {//Need to rotate root
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
       // return rotateCount;
    }

    void transplant(Node u, Node v){ 
          if(u.parent == nil){
              root = v;
          }else if(u == u.parent.left){
              u.parent.left = v;
          }else
              u.parent.right = v;
          v.parent = u.parent;
    }
    boolean delete(Node z){
        recolorCount = 0;
      
        if((z = findNode(z, root))==null)return false;
        Node x;
        Node y = z; 
        int y_original_color = y.color;
        if(z.left==nil){
        x = z.left;
        transplant(z,z.right);
        }
        else if(z.right==nil){
            x = z.left;
            transplant(z,z.left);
        }
        else{ y = treeMinimum(z.right);
            y_original_color = y.color;
            recolorCount++;
            x = y.right;
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
            recolorCount++;
        }
        int re;
        if(y_original_color==BLACK)
        {  re =  deleteFixup(x, recolorCount);
            System.out.println("Recolour count after deletion : " +re);
        }  
        return true;
        
    }
       int deleteFixup(Node x, int reColor){
           Node w;
          // rotateCount = 0;
        while(x!=root && x.color == BLACK){ 
            if(x == x.parent.left){
                 w = x.parent.right;
                if(w.color == RED){
                    reColor++;
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    reColor++;
                    continue;
                }
                else if(w.right.color == BLACK){
                    w.left.color = BLACK;
                    w.color = RED;
                    reColor++;
                    rotateRight(w);
                   // rotateCount++;
                    w = x.parent.right;
                }
                //if(w.right.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    reColor++;
                    rotateLeft(x.parent);
                    x = root;
                //}
            }else{
                w = x.parent.left;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    reColor++;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK){
                    w.color = RED;
                    reColor++;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK){
                    w.right.color = BLACK;
                    w.color = RED;
                    reColor++;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                //w.left.color == RED;
                w.color = x.parent.color;
                x.parent.color = BLACK;
                w.left.color = BLACK;
                reColor++;
                rotateRight(x.parent);
                x = root;
            }
        }
        x.color = BLACK; 
        reColor++;
        return reColor;
    }    Node treeMinimum(Node subTree){
        while(subTree.left!=nil){
            subTree = subTree.left;
        }
        return subTree;
    }
    private Node findNode(Node findNode, Node node) {
        int probe = 0;
        if (root == nil) {
            probe++;
            System.out.println("No. of probes: " +probe);
            return null;
        }

        if (findNode.key < node.key) {
            probe++;
            System.out.println("No. of probes: " +probe);
            if (node.left != nil) {
                probe++;
                System.out.println("No. of probes: " +probe);
                return findNode(findNode, node.left);
            }
        } else if (findNode.key > node.key) {
            probe++;
            System.out.println("No. of probes: " +probe);
            if (node.right != nil) {
                probe++;
                System.out.println("No. of probes: " +probe);
                return findNode(findNode, node.right);
            }
        } else if (findNode.key == node.key) {
            probe++;
            System.out.println("No. of probes: " +probe);
            return node;
        }
        
        return null;
    }
    private void read(){
    Scanner sc = new Scanner(System.in);
        int choice = 0;
        int item;
        Node node;
        while(choice != 5){
        System.out.println("------MENU------");
        System.out.println("1. Insert \n2. Print Tree\n3. Find Element\n4. Delete Element\n5. Exit");
       // System.out.println("Enter choice: ");
        choice = sc.nextInt();
        switch(choice){
            case 1: item = sc.nextInt();
                    while(item!=-999){
                        node = new Node(item);
                        insert(node);
                        item = sc.nextInt();}
                    printTree(root);
                    break;
            case 2: printTree(root);
                    break;
            case 3:
                    item = sc.nextInt();
                    while (item != -999) {
                        node = new Node(item);
                        System.out.println((findNode(node, root) != null) ? "Found!!!" : "Not found!!!");
                        item = sc.nextInt();
                    }
                    break;
            case 4: item = sc.nextInt();
                    while (item != -999) {
                        node = new RedBlackTree.Node(item);
                        //System.out.print("\nDeleting item " + item);
                        if (delete(node)) {
                            System.out.print("Node "+item+" Deleted!!!");
                        } else {
                            System.out.print("Node "+item+" does not exist!");
                        }
                        item = sc.nextInt();
                    }
                    System.out.println();
                    printTree(root);
                    break;
            case 5: break;
        }
    }
    }
    public static void main(String[] args) {
        // TODO code application logic here
        RedBlackTree rbt = new RedBlackTree();
        rbt.read();
    }
    
    
}
