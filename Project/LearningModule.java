import javax.swing.*;
import java.awt.*;

// Interface for page navigation
interface PageNavigator {
    void nextPage();
    void previousPage();
}

// Interface for content management
interface ContentManager {
    void NewPages();
}

// Main class for the learning application
public class LearningModule extends JFrame implements PageNavigator, ContentManager {
    JButton start, next, previous, exit;
    JLabel imageLabel, headingLabel, pageLabel;
    JPanel contentPanel;
    int currentPage = 0;

    // Ensure the image path is correct when run
    String[] imagePaths = {
        "C:\\Users\\Eddy Lau\\OneDrive\\Documents\\Y2 sem2\\LearningModule\\Image\\Introduction.png",
        "C:\\Users\\Eddy Lau\\OneDrive\\Documents\\Y2 sem2\\LearningModule\\Image\\Declaration.png",
        "C:\\Users\\Eddy Lau\\OneDrive\\Documents\\Y2 sem2\\LearningModule\\Image\\Initialise.png",
        "C:\\Users\\Eddy Lau\\OneDrive\\Documents\\Y2 sem2\\LearningModule\\Image\\Access.png",
        "C:\\Users\\Eddy Lau\\OneDrive\\Documents\\Y2 sem2\\LearningModule\\Image\\Update.png",
        "C:\\Users\\Eddy Lau\\OneDrive\\Documents\\Y2 sem2\\LearningModule\\Image\\1D.png",
        "C:\\Users\\Eddy Lau\\OneDrive\\Documents\\Y2 sem2\\LearningModule\\Image\\Multidimension.png",
        "C:\\Users\\Eddy Lau\\OneDrive\\Documents\\Y2 sem2\\LearningModule\\Image\\Access.png",
        "C:\\Users\\Eddy Lau\\OneDrive\\Documents\\Y2 sem2\\LearningModule\\Image\\Function.png",
        "C:\\Users\\Eddy Lau\\OneDrive\\Documents\\Y2 sem2\\LearningModule\\Image\\Properties.png",
        "C:\\Users\\Eddy Lau\\OneDrive\\Documents\\Y2 sem2\\LearningModule\\Image\\Ad_Dis.png"
    };

    String[] headings = {
        "1. Introduction of Array",
        "2. Declaration of Array",
        "3. Array Initialization",
        "4. Accessing Array Elements",
        "5. Updating Array Elements",
        "6. One Dimensional Array",
        "7. Multidimensional Array",
        "8. Accessing Multidimensional Array",
        "9. Passing Arrays to Functions",
        "10. Properties of Array in C",
        "11. Advantages & Disadvantages of Array"
    }; 

    String[][] contents = {
        {
            "An array in C is a fixed-size collection of similar data items",
            "stored in contiguous memory locations.",
            "It can be used to store the collection of primitive data types",
            "such as int, char, float, etc."
        },
        {
            "Declare the array before using it.",
            "We can declare the array by specifying its name, elements,",
            "size of the dimensions."
        },
        {
            "Initialization is the process to assign",
            "some initial value to the variable."
        },
        {
            "We can access any element of an array in C using the array",
            "subscript operator [ ] and the index value i", 
            "of the element."
        },
        {
            "We can update the array at the given index i."
        },
        {
            "1D array known as the array only has one dimension."
        },
        {
            "Multidimensional array known as the array has",
            "more than one dimension.",
            "For example, 2D array and 3D array."
        },
        {
            "Accessing an element of a multi-dimensional array,",
            "specify an index",
            "number in each of the array's dimensions."
        },
        {
            "An array is always passed as pointers to a function in C."
        },
        {
            ""
        },
        {
            ""
        }
    };

    LearningPages learningPages;

    public LearningModule() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Image display
        ImageIcon imageIcon = new ImageIcon(imagePaths[currentPage]);
        Image image = imageIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setBounds(10, 10, 300, 200);
        add(imageLabel);

        // Heading display
        headingLabel = new JLabel(headings[currentPage]);
        headingLabel.setBounds(10, 220, 360, 30);
        headingLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        headingLabel.setForeground(new Color(0, 0, 0));
        add(headingLabel);

        // Content panel
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBounds(10, 260, 360, 200);
        add(contentPanel);

        // Page label
        pageLabel = new JLabel();
        pageLabel.setBounds(10, 470, 360, 20);
        pageLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        pageLabel.setForeground(new Color(0, 0, 0));
        add(pageLabel);

        // Navigation buttons
        previous = new JButton("Previous");
        previous.setBounds(10, 500, 100, 30);
        previous.addActionListener(e -> previousPage());
        add(previous);

        next = new JButton("Next");
        next.setBounds(130, 500, 100, 30);
        next.addActionListener(e -> nextPage());
        add(next);

        exit = new JButton("Exit");
        exit.setBounds(250, 500, 100, 30);
        exit.addActionListener(e -> System.exit(0));
        add(exit);

        // Initialize ContentManager
        learningPages = new LearningPages(this);

        // Set title with module title
        setTitle(learningPages.getModuleTitle());

        setSize(400, 600);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Initially disable previous button on first page
        previous.setEnabled(false);

        NewPages();
    }

    public static void main(String[] args) {
        new LearningModule();
    }

    @Override
    public void nextPage() {
        if (currentPage < headings.length - 1) {
            currentPage++;
            NewPages();
        }
    }

    @Override
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            NewPages();
        }
    }

    @Override
    public void NewPages() {
        // New image
        ImageIcon imageIcon = new ImageIcon(imagePaths[currentPage]);
        Image image = imageIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(image));

        // New heading
        headingLabel.setText(headings[currentPage]);

        // New content
        contentPanel.removeAll();
        for (String line : contents[currentPage]) {
            JLabel contentLabel = new JLabel(line);
            contentLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(contentLabel);
        }
        contentPanel.revalidate();
        contentPanel.repaint();

        // Update page label
        pageLabel.setText(String.format("Page %d of %d", currentPage + 1, learningPages.getTotalPages()));

        // Enable/disable previous and next buttons based on current page
        previous.setEnabled(currentPage > 0);
        next.setEnabled(currentPage < headings.length - 1);
    }
}

// Class for managing the content updates
class LearningPages implements ContentManager {
    private LearningModule learningModule;
    private int totalPages;
    private String moduleTitle;

    public LearningPages(LearningModule learningModule) {
        this.learningModule = learningModule;
        this.totalPages = learningModule.headings.length;
        this.moduleTitle = "Learning Module";
    }

    @Override
    public void NewPages() {
        learningModule.NewPages();
    }

    public int getTotalPages() {
        return totalPages;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }
}

