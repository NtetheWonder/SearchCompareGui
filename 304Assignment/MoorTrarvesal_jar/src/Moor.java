package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Moor extends JPanel {

    public char[][] moor;
    Scanner input;
    String ur;
    LinkedList<Node> path = new LinkedList<>();
    LinkedList<Node> shortPath = new LinkedList<>();
    LinkedList<Node> finish = new LinkedList<>();
    LinkedList<Node> goal = new LinkedList<>();

    public Moor(String ur) {

        this.ur = ur;
        setMoor(ur);

        setPreferredSize(new Dimension(600, 550));
        setBackground(Color.GRAY);


        repaint();
    }

    public boolean land(int r, int c) {
        return (r >= 0) && (r < moor.length) &&
                (c >= 0) && (c < moor[0].length) &&
                moor[r][c] == 'D';
    }

    public boolean water(int r, int c) {
        return (r >= 0) && (r < moor.length) &&
                (c >= 0) && (c < moor[0].length) &&
                moor[r][c] == 'W';
    }

    public void setMoor(String ur) {
        this.ur = ur;
        path.clear();
        goal.clear();
        shortPath.clear();
        finish.clear();
        try {

            input = new Scanner(new File(ur));

            int rows = Integer.parseInt(input.next());
            int cols = Integer.parseInt(input.next());
            moor = new char[rows][cols];

            while (input.hasNext()) {

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        moor[i][j] = input.next().charAt(0);
                    }

                }
            }

        } catch (FileNotFoundException e) {
            System.out.print("File not found");
        }
        repaint();

    }
    public class Node implements Comparable {


        int col = 0;
        int row = 0;
        int cost = 0;
        double heuristic =0 ;
        Node parent;

        public Node(Node parent , int row , int col) {
            this.col =col;
            this.row =row;
            this.parent =parent;

        }
        public String toString(){

            return ("("+row+","+col+") ");
        }

        @Override
        public int compareTo(Object o) {
            return 0;
        }
    }

    public int getNumRows(){
        return moor.length ;
    }
    public int getCol(){
        return moor[0].length;
    }
    public int getPathSize(){
        return path.size();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < moor.length; i++) {
            for (int j = 0; j < moor[0].length; j++) {

                if (land(i, j))
                    g.setColor(Color.WHITE);

                if (moor[i][j] == '+')
                    g.setColor(Color.MAGENTA);
                if (water(i, j))
                    g.setColor(Color.cyan);

                g.fillRect((j * 50), (i * 50), 50, 50);
                g.setColor(Color.GRAY);
                g.drawRect((j * 50), (i * 50), 50, 50);

            }

        }
    }


    void ToString() {

        for (int r = 0; r < moor.length; r++) {
            for (int c = 0; c < moor[0].length; c++)
                System.out.print(moor[r][c] + " ");
            System.out.println();
        }

    }


    void DFS(int row, int col) {
        path.clear();
        Boolean vis[][] = new Boolean[moor.length][moor[0].length];
        // Initialize a stack of pairs and
        // push the starting cell into it
        Stack<Node> st = new Stack<Node>();
        st.push(new Node(null, row, col));

        // Iterate until the
        // stack is not empty
        while (!st.empty()) {

            // Pop the top pair
            Node curr = st.pop();

            row = curr.row;
            col = curr.col;

            // Check if the current popped
            // cell is a valid cell or not
            if (!land(row, col))
                continue;

            // Mark the current
            // cell as visited
            vis[row][col] = true;
            path.add(curr);
            if (curr.col == moor[0].length - 1) {
                return;
            }
            int[][] neb = {{row, col + 1}, {row + 1, col + 1}, {row - 1, col + 1}};
            // Push all the adjacent cells
            for (int[] i : neb) {
                if (land(i[0], i[1])) {
                    int adjx = i[0];
                    int adjy = i[1];
                    st.push(new Node(curr, adjx, adjy));
                }
            }
        }
    }

    void myPath() {
        for (Node i : shortPath) {
            moor[i.row][i.col] = '+';
        }
    }

    void bestPath() {
        for (Node i : path) {
            moor[i.row][i.col] = '+';
        }
    }


    void ShortPath() {
        Node goal = path.getLast();
        shortPath.add(goal);
        while (goal.col != 0) {
            goal = goal.parent;
            shortPath.add(goal);
        }
    }

    LinkedList<Node> goalState() {
        for(int i =0 ;i< moor.length ;i++) {
            if (moor[i][0] == 'D') {
                goal.add(new Node(null, i, 0));
            }

        }
        return goal;
    }
    void ExitStates() {
        finish.clear();
        for (int i = 0; i < moor[0].length; i++) {
            if (land(i, moor[0].length - 1))
                finish.add(new Node(null,i, moor[0].length - 1));
        }
    }


    void BFS(int row, int col) {

        path.clear();
        // Stores indices of the matrix cells
        Queue<Node> q = new LinkedList<>();
        Node head = new Node(null, row, col);

        // Mark the starting cell as visited
        // and push it into the queue
        q.add(head);
        path.add(head);
        Boolean vis[][] = new Boolean[moor.length][moor[0].length];

        vis[row][col] = true;

        // Iterate while the queue
        // is not empty

        while (!q.isEmpty()) {
            Node cell = q.remove();
            int x = cell.row;
            int y = cell.col;

            int[][] neb = {{x, y + 1}, {x + 1, y + 1}, {x - 1, y + 1}};
            // Go to the adjacent cells
            for (int[] i : neb) {

                int adjx = i[0];
                int adjy = i[1];

                if (land(adjx, adjy)) {
                    Node curr = new Node(cell, adjx, adjy);
                    q.add(curr);
                    vis[adjx][adjy] = true;
                    path.add(curr);
                    if (adjy == moor[0].length - 1) {
                        return;
                    }
                }

            }

        }

    }

    void best_first_search(int source, int n) {

        Boolean[][] visited = new Boolean[moor.length][moor[0].length];
        // MIN HEAP priority queue
        Queue<Node> pq = new LinkedList<>();
        Node head = new Node(null, source, n);
        // sorting in pq gets done by first value of pair
        pq.add(head);
        path.add(head);

        int s = source;
        visited[s][n] = true;
        while (!pq.isEmpty()) {
            Node u = pq.remove();

            path.add(u);
            if (u.col == moor[0].length -1)
                return;

            int[][] neb = {{u.row, u.col + 1}, {u.row - 1, u.col + 1}, {u.row + 1, u.col + 1}};
            for (int[] i : neb) {

                int newX = i[0];
                int newY = i[1];

                if (land(newX, newY)) {
                    Node curr = new Node(u, newX, newY);
                    curr.heuristic = Math.sqrt((Math.pow((newX - goal.indexOf(0)), 2) + (Math.pow((newY - goal.indexOf(1)), 2))));

                    visited[newX][newY] = true;
                    pq.add(curr);
                }
            }
        }
    }
    String swPath(){
        String rs ="";
        for(int p =shortPath.size()-1 ;p >= 0 ;p--){
            String pp = shortPath.get(p).toString();
            rs = rs+ pp ;
        }
        return rs ;
    }

    void AStar_Search ( Node start, Node goals){
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        LinkedList<Node> explored = new LinkedList<>();
        Node head = new Node(null, start.row, start.col);
        head.heuristic = Math.sqrt((Math.pow((start.row - goal.indexOf(0)), 2) + (Math.pow((start.col - goal.indexOf(1)), 2))));
        frontier.add(head);
        //explored is where we 've been
        explored.add(new Node(null, head.row, head.col));
        path.add(head);

        if (head.col == moor[0].length - 1)
            return;

        //keep going while there is more to explore
        while (frontier.size() > 0) {
            Node current_node = frontier.poll();
            //if we found the goal, we 're done

            int[][] neb = {{current_node.row, current_node.col + 1}, {current_node.row - 1, current_node.col + 1},
                    {current_node.row + 1, current_node.col + 1}};
            //check where we can go next and haven 't explored
            for (int[] child : neb) {

                if (land(child[0], child[1])) {
                    explored.add(new Node(null, child[0], child[1]));
                    Node newNode = new Node(current_node, child[0], child[1]);
                    newNode.heuristic = Math.sqrt((Math.pow((child[0] - goal.indexOf(0)), 2) + (Math.pow((child[1] - goal.indexOf(1)), 2))));
                    frontier.add(newNode);
                    path.add(current_node);

                }
            }
        }

        return;  //went through everything and never found goal

    }


    public static void main(String[] args) {

        JPanel p =new JPanel();
        JFrame frame =new JFrame();
        JButton search =new JButton("Load File");
        JTextField file = new JTextField();
        JTextField dPath = new JTextField();
        JLabel sp = new JLabel("The shortest path is :");

        dPath.setPreferredSize(new Dimension(250,60));

        p.setPreferredSize(new Dimension(200,40));
        //p.setBackground(Color.GRAY);
        file.setPreferredSize(new Dimension(100,30));
        search.setPreferredSize(new Dimension(100,30));
        p.add(file,BorderLayout.EAST);
        p.add(search,BorderLayout.WEST);
        JPanel pn =new JPanel();
        JPanel pPath =new JPanel();
        JPanel pC = new JPanel();

        pC.setPreferredSize(new Dimension(200,200));
        Moor m  =new Moor("A");
        Moor n= new Moor("A");


        pC.add(sp,BorderLayout.EAST);
        String[] petStrings = { "depth-first search","breadth-first search","best first search","A∗"};
        frame.setBackground(Color.GRAY);

        JComboBox petList = new JComboBox(petStrings);

        frame.setBackground(Color.GRAY);
        pn.setBackground(Color.GRAY);
        pPath.setBackground(Color.GRAY);

        pn.setPreferredSize(new Dimension(800, 600));
        pPath.setPreferredSize(new Dimension(800, 600));
        petList.setSelectedIndex(0);
        petList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //n =new com.company.Moor(file.getText().toString());
                n.setMoor(file.getText().toString());
                n.path.clear();
                n.shortPath.clear();
                n.goalState();
                n.ExitStates();

                SwingUtilities.updateComponentTreeUI(frame);
                String sel = petList.getSelectedItem().toString();

                if(sel =="depth-first search") {

                    for(Node i :n.goal) {
                        n.DFS(i.row, i.col);

                        if (n.path.getLast().col >= n.getCol() - 1)
                            break;

                    }

                }
                else if(sel =="breadth-first search"){

                    for(Node i : n.goal) {
                        n.BFS(i.row, i.col);

                        if (n.path.getLast().col >= n.getCol() - 1)
                            break;
                    }

                }
                else if(sel =="best first search"){

                    for(Node i : n.goal) {
                        n.best_first_search(i.row, i.col);

                        if (n.path.getLast().col >= n.getCol() - 1)
                            break;
                    }

                }
                else if(sel =="A∗"){

                    for(Node i : n.goal) {
                        for(Node j: n.finish)
                        n.AStar_Search(i, j);

                        if (n.path.getLast().col >= n.getCol() - 1)
                            break;

                    }
                }


                n.ShortPath();
                n.myPath();
                System.out.println();


                pPath.add(n);

                System.out.println("-----------------");
                n.ToString();
                //n.showPath();
                //n.swPath();
                dPath.setText(n.swPath());
            }
        });
        p.add(petList);

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                m.setMoor(file.getText().toString());
                SwingUtilities.updateComponentTreeUI(frame);

                System.out.println();
                pn.setPreferredSize(new Dimension(600, 550));
                pn.add(m);

                System.out.println("-----------------");
                m.ToString();

            }
        });

        pC.add(dPath,BorderLayout.EAST);

        frame.setSize(1400,800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(p,BorderLayout.NORTH);
        frame.getContentPane().add(pn,BorderLayout.WEST);
        frame.getContentPane().add(pPath,BorderLayout.EAST);
        frame.getContentPane().add(pC,BorderLayout.SOUTH);

        frame.setVisible(true);

    }

}

