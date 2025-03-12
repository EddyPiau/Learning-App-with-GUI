import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// LearningNavigator Interface
interface LearningNavigator {
    void nextPage();
    void previousPage();
}

// ContentUpdater Interface
interface ContentUpdater {
    void updateContent();
}

// AppNavigator Interface
interface AppNavigator {
    void navigateTo(String screen);
}

// MainScreenHandler Interface
interface MainScreenHandler {
    void initialize();
}

// QuizNavigator Interface
interface QuizNavigator {
    void showNextQuestion();
    void showPreviousQuestion();
    void submitQuiz();
}

// QuestionHandler Interface
interface QuestionHandler {
    void loadQuestions();
    void saveSelectedAnswer();
    void updateQuestion();
}

// LeaderboardHandler Interface
interface LeaderboardHandler {
    void displayLeaderboard();
}

// TimerHandler Interface
interface TimerHandler {
    void startTimer();
    void stopTimer();
}

// LearningApp Class
class LearningApp extends JFrame implements AppNavigator {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JPanel mainPanel;

    public LearningApp() {
        setTitle("Learning App");
        setSize(360, 640); // Smartphone resolution
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        // Main screen
        MainScreen mainScreen = new MainScreen(this, cardLayout, contentPanel);
        contentPanel.add(mainScreen, "Home");
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void navigateTo(String screen) {
        cardLayout.show(contentPanel, screen);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LearningApp::new);
    }
}

// MainScreen Class
class MainScreen extends JPanel implements MainScreenHandler {
    private AppNavigator appNavigator;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JLabel courseLabel;
    private JLabel subtopicLabel;
    private JButton learnButton;
    private JButton quizButton;
    private JButton leaderboardButton;
    private JButton exitAppButton;

    public MainScreen(AppNavigator appNavigator, CardLayout cardLayout, JPanel contentPanel) {
        this.appNavigator = appNavigator;
        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;
        initialize();
    }

    @Override
    public void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        courseLabel = new JLabel("Course: Introduction to Programming");
        subtopicLabel = new JLabel("Subtopic: Array");

        courseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtopicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        learnButton = new JButton("Click to learn");
        quizButton = new JButton("Take a quiz");
        leaderboardButton = new JButton("View Leaderboard");
        exitAppButton = new JButton("Exit App");

        learnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quizButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitAppButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        setupButtons();

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(courseLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(subtopicLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(learnButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(quizButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(leaderboardButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(exitAppButton);
    }

    private void setupButtons() {
        learnButton.addActionListener(e -> {
            LearningModule learningModule = new LearningModule(cardLayout, contentPanel);
            contentPanel.add(learningModule, "Learning");
            appNavigator.navigateTo("Learning");
        });

        quizButton.addActionListener(e -> {
            String studentName = JOptionPane.showInputDialog(this, "Enter your name:");
            if (studentName != null && !studentName.trim().isEmpty()) {
                QuizManager quizManager = new QuizManager(studentName);
                QuizModule quizModule = new QuizModule(cardLayout, contentPanel, quizManager);
                contentPanel.add(quizModule, "Quiz");
                appNavigator.navigateTo("Quiz");
            }
        });

        leaderboardButton.addActionListener(e -> {
            LeaderboardG leaderboardModule = new LeaderboardG(cardLayout, contentPanel);
            contentPanel.add(leaderboardModule, "Leaderboard");
            appNavigator.navigateTo("Leaderboard");
        });

        exitAppButton.addActionListener(e -> System.exit(0));
    }
}

// LearningModule Class
class LearningModule extends JPanel implements LearningNavigator, ContentUpdater {
    private JButton next, previous, exit;
    private JLabel imageLabel, headingLabel, pageLabel;
    private JPanel contentPanel;
    private int currentPage = 0;

    private String[] imagePaths = {
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

    private String[] headings = {
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

    private String[][] contents = {
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

    private LearningPages learningPages;
    private CardLayout cardLayout;
    private JPanel contentPane;

    public LearningModule(CardLayout cardLayout, JPanel contentPane) {
        this.cardLayout = cardLayout;
        this.contentPane = contentPane;

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
        exit.addActionListener(e -> cardLayout.show(contentPane, "Home"));
        add(exit);

        // Initialize ContentUpdater
        learningPages = new LearningPages(this);

        // Initially disable previous button on first page
        previous.setEnabled(false);

        updateContent();
    }

    @Override
    public void nextPage() {
        if (currentPage < headings.length - 1) {
            currentPage++;
            updateContent();
        }
    }

    @Override
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateContent();
        }
    }

    @Override
    public void updateContent() {
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

    public String[] getHeadings() {
        return headings;
    }
}

// LearningPages Class
class LearningPages implements ContentUpdater {
    private LearningModule learningModule;
    private int totalPages;
    private String moduleTitle;

    public LearningPages(LearningModule learningModule) {
        this.learningModule = learningModule;
        this.totalPages = learningModule.getHeadings().length;
        this.moduleTitle = "Learning Module";
    }

    @Override
    public void updateContent() {
        learningModule.updateContent();
    }

    public int getTotalPages() {
        return totalPages;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }
}

// QuizModule Class
class QuizModule extends JPanel implements QuizNavigator, QuestionHandler {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private QuizManager quizManager;
    private TimerG timerG;

    private JLabel questionLabel;
    private JLabel questionNumberLabel;
    private JPanel optionsPanel;
    private JTextField fillInTheBlankField;
    private JRadioButton[] options;

    public QuizModule(CardLayout cardLayout, JPanel contentPanel, QuizManager quizManager) {
        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;
        this.quizManager = quizManager;

        setLayout(null);

        timerG = new TimerG();
        add(timerG.getTimerLabel());

        // Quiz panel
        questionLabel = new JLabel("", JLabel.CENTER);
        questionLabel.setBounds(10, 50, 340, 30);
        add(questionLabel);

        questionNumberLabel = new JLabel("", JLabel.CENTER);
        questionNumberLabel.setBounds(10, 80, 340, 30);
        add(questionNumberLabel);

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBounds(10, 120, 340, 200);
        ButtonGroup buttonGroup = new ButtonGroup();

        options = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            buttonGroup.add(options[i]);
            optionsPanel.add(options[i]);
        }
        add(optionsPanel);

        fillInTheBlankField = new JTextField();
        fillInTheBlankField.setBounds(10, 120, 340, 30);
        add(fillInTheBlankField);
        fillInTheBlankField.setVisible(false);

        JButton previousButton = new JButton("Previous");
        previousButton.setBounds(10, 500, 100, 30);
        previousButton.addActionListener(e -> showPreviousQuestion());
        add(previousButton);

        JButton nextButton = new JButton("Next");
        nextButton.setBounds(130, 500, 100, 30);
        nextButton.addActionListener(e -> showNextQuestion());
        add(nextButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(250, 500, 100, 30);
        exitButton.addActionListener(e -> {
            timerG.stopTimer();
            cardLayout.show(contentPanel, "Home");
        });
        add(exitButton);

        updateQuestion();
    }

    @Override
    public void loadQuestions() {
        quizManager.loadQuestions();
    }

    @Override
    public void saveSelectedAnswer() {
        if (fillInTheBlankField.isVisible()) {
            quizManager.saveAnswer(fillInTheBlankField.getText().trim());
        } else {
            for (JRadioButton option : options) {
                if (option.isVisible() && option.isSelected()) {
                    quizManager.saveAnswer(option.getText().trim());
                    break; // Exit loop once the selected answer is found
                }
            }
        }
    }

    @Override
    public void updateQuestion() {
        String[] questionData = quizManager.getCurrentQuestion();
        questionLabel.setText(questionData[0]);
        questionNumberLabel.setText("Question " + (quizManager.getCurrentQuestionIndex() + 1) + "/" + quizManager.getTotalQuestions());
        optionsPanel.setVisible(true);
        fillInTheBlankField.setVisible(false);

        for (int i = 0; i < options.length; i++) {
            if (i < questionData.length - 1) {
                options[i].setVisible(true);
                options[i].setText(questionData[i + 1]);
                options[i].setSelected(questionData[i + 1].equals(quizManager.getCurrentAnswer()));
            } else {
                options[i].setVisible(false);
            }
        }

        if (questionData[0].contains("Fill in the blank")) {
            optionsPanel.setVisible(false);
            fillInTheBlankField.setVisible(true);
            fillInTheBlankField.setText(quizManager.getCurrentAnswer() != null ? quizManager.getCurrentAnswer() : "");
        }
    }

    @Override
    public void showNextQuestion() {
        if (quizManager.hasNextQuestion()) {
            saveSelectedAnswer();
            quizManager.nextQuestion();
            updateQuestion();
        } else {
            submitQuiz();
        }
    }

    @Override
    public void showPreviousQuestion() {
        if (quizManager.hasPreviousQuestion()) {
            saveSelectedAnswer();
            quizManager.previousQuestion();
            updateQuestion();
        }
    }

    @Override
    public void submitQuiz() {
        timerG.stopTimer();
        quizManager.saveScore();
        showQuizResults();
    }

    private void showQuizResults() {
        int score = quizManager.getScore();
        int totalQuestions = quizManager.getTotalQuestions();

        double percentage = ((double) score / totalQuestions) * 100;
        int stars = (int) (percentage / 20);

        String message;
        if (percentage >= 80) {
            message = "Outstanding!";
        } else if (percentage >= 60) {
            message = "That's good!";
        } else if (percentage >= 40) {
            message = "Good try!";
        } else if (percentage >= 20) {
            message = "You can do better!";
        } else {
            message = "Don't give up!";
        }

        JPanel resultPanel = new JPanel(new BorderLayout());
        JLabel scoreLabel = new JLabel("Your score: " + score + "/" + totalQuestions + " (" + (int) percentage + "%)", JLabel.CENTER);
        scoreLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        JLabel messageLabel = new JLabel(message, JLabel.CENTER);
        messageLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));

        JPanel starPanel = new JPanel();
        starPanel.setLayout(new BoxLayout(starPanel, BoxLayout.X_AXIS));
        for (int i = 0; i < stars; i++) {
            starPanel.add(new JLabel(new ImageIcon("star.png"))); // Assuming you have a star image file
        }

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.add(scoreLabel);
        middlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        middlePanel.add(messageLabel);
        middlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        middlePanel.add(starPanel);

        resultPanel.add(middlePanel, BorderLayout.CENTER);

        JLabel leaderboardLabel = new JLabel("Leaderboard", JLabel.CENTER);
        leaderboardLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        resultPanel.add(leaderboardLabel, BorderLayout.NORTH);

        // Create the leaderboard
        LeaderboardG leaderboardModule = new LeaderboardG(cardLayout, contentPanel);
        JPanel leaderboardPanel = leaderboardModule.createLeaderboardPanel();

        resultPanel.add(leaderboardPanel, BorderLayout.SOUTH);

        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 500, 100, 30);
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "Home"));
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(250, 500, 100, 30);
        exitButton.addActionListener(e -> cardLayout.show(contentPanel, "Home"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(backButton);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(exitButton);

        resultPanel.add(bottomPanel, BorderLayout.NORTH);

        removeAll();
        setLayout(new BorderLayout());
        add(resultPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}

// QuizManager Class
class QuizManager {
    private List<String[]> questions;
    private String[] correctAnswers;
    private String[] userAnswers;
    private int currentQuestionIndex = 0;
    private String studentName;

    public QuizManager(String studentName) {
        this.studentName = studentName;
        loadQuestions();
        userAnswers = new String[questions.size()];
    }

    public void loadQuestions() {
        questions = new ArrayList<>();
        questions.add(new String[]{"What is an array?", "A data structure", "A function", "A loop", "A class", "A data structure"});
        questions.add(new String[]{"How do you declare an array in C?", "int array[10];", "int array = [10];", "int array{10};", "int array(10);", "int array[10];"});
        questions.add(new String[]{"What is the index of the first element in an array?", "1", "0", "-1", "2", "0"});
        questions.add(new String[]{"Which of the following is a multidimensional array?", "int array[10];", "int array[5][5];", "int array = [10];", "int array(10);", "int array[5][5];"});
        questions.add(new String[]{"How do you access the 3rd element of an array?", "array[2]", "array[3]", "array[1]", "array[4]", "array[2]"});
        questions.add(new String[]{"What is the advantage of an array?", "Fixed size", "Efficient data storage", "Dynamic size", "None", "Efficient data storage"});
        questions.add(new String[]{"What is the disadvantage of an array?", "Dynamic size", "Fixed size", "Efficient data storage", "None", "Fixed size"});
        questions.add(new String[]{"How do you initialize an array?", "int array[5] = {1, 2, 3, 4, 5};", "int array(5) = {1, 2, 3, 4, 5};", "int array{5} = {1, 2, 3, 4, 5};", "int array = [1, 2, 3, 4, 5];", "int array[5] = {1, 2, 3, 4, 5];"});
        questions.add(new String[]{"Can you pass an array to a function?", "Yes", "No", "Maybe", "None", "Yes"});
        questions.add(new String[]{"What is the index of the last element in an array of size 10?", "10", "9", "8", "7", "9"});
        questions.add(new String[]{"How do you update an element in an array?", "array[1] = 10;", "array[1] = {10};", "array{1} = 10;", "array(1) = 10;", "array[1] = 10;"});
        questions.add(new String[]{"What is a one-dimensional array?", "A list of elements", "An array of arrays", "A 2D array", "None", "A list of elements"});
        questions.add(new String[]{"What is a multi-dimensional array?", "An array of arrays", "A list of elements", "A 1D array", "None", "An array of arrays"});
        questions.add(new String[]{"How do you declare a two-dimensional array?", "int array[10][10];", "int array[10];", "int array = [10][10];", "int array{10][10};", "int array[10][10];"});
        questions.add(new String[]{"How do you access an element in a two-dimensional array?", "array[1][1]", "array[1]", "array[1, 1]", "array[1]{1}", "array[1][1]"});
        questions.add(new String[]{"What is a property of arrays in C?", "Fixed size", "Dynamic size", "No size limit", "None", "Fixed size"});
        questions.add(new String[]{"What is an advantage of arrays?", "Efficient data storage", "Fixed size", "Dynamic size", "None", "Efficient data storage"});
        questions.add(new String[]{"What is a disadvantage of arrays?", "Fixed size", "Efficient data storage", "Dynamic size", "None", "Fixed size"});
        questions.add(new String[]{"How do you pass an array to a function?", "By specifying the array name", "By specifying the array size", "By specifying the array type", "None", "By specifying the array name"});
        questions.add(new String[]{"What is the purpose of an array?", "To store multiple values", "To store a single value", "To perform calculations", "None", "To store multiple values"});
        
        // Fill-in-the-blanks questions
        questions.add(new String[]{"Fill in the blank: An array is a ______ of similar data items.", "", "", "", "", "collection"});
        questions.add(new String[]{"Fill in the blank: The first index of an array is ______.", "", "", "", "", "0"});

        correctAnswers = new String[]{
            "A data structure", "int array[10];", "0", "int array[5][5];", "array[2]", "Efficient data storage", "Fixed size", "int array[5] = {1, 2, 3, 4, 5];",
            "Yes", "9", "array[1] = 10;", "A list of elements", "An array of arrays", "int array[10][10];", "array[1][1]", "Fixed size",
            "Efficient data storage", "Fixed size", "By specifying the array name", "To store multiple values",
            "collection", "0"
        };
    }

    public String[] getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public boolean hasNextQuestion() {
        return currentQuestionIndex < questions.size() - 1;
    }

    public boolean hasPreviousQuestion() {
        return currentQuestionIndex > 0;
    }

    public void nextQuestion() {
        if (hasNextQuestion()) {
            currentQuestionIndex++;
        }
    }

    public void previousQuestion() {
        if (hasPreviousQuestion()) {
            currentQuestionIndex--;
        }
    }

    public String getCurrentAnswer() {
        return userAnswers[currentQuestionIndex];
    }

    public void saveAnswer(String answer) {
        userAnswers[currentQuestionIndex] = answer;
    }

    public void saveScore() {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i] != null && userAnswers[i].equalsIgnoreCase(correctAnswers[i].trim())) {
                score++;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("student_scores.txt", true))) {
            writer.write(studentName + ": " + score + "/" + questions.size());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getScore() {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i] != null && userAnswers[i].equalsIgnoreCase(correctAnswers[i].trim())) {
                score++;
            }
        }
        return score;
    }
}

// LeaderboardG Class
class LeaderboardG extends JPanel implements LeaderboardHandler {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JLabel leaderboardLabel;
    private JTable table;

    public LeaderboardG(CardLayout cardLayout, JPanel contentPanel) {
        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;
        displayLeaderboard();
    }

    @Override
    public void displayLeaderboard() {
        setLayout(new BorderLayout());

        leaderboardLabel = new JLabel("Leaderboard", JLabel.CENTER);
        leaderboardLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        add(leaderboardLabel, BorderLayout.NORTH);

        JPanel leaderboardPanel = createLeaderboardPanel();
        add(leaderboardPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "Home"));
        add(backButton, BorderLayout.SOUTH);
    }

    public JPanel createLeaderboardPanel() {
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));

        List<String> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("student_scores.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sort scores
        scores.sort((a, b) -> {
            int scoreA = Integer.parseInt(a.split(": ")[1].split("/")[0]);
            int scoreB = Integer.parseInt(b.split(": ")[1].split("/")[0]);
            return Integer.compare(scoreB, scoreA);
        });

        String[] columnNames = {"Name", "Score"};
        String[][] data = new String[scores.size()][2];
        for (int i = 0; i < scores.size(); i++) {
            String[] parts = scores.get(i).split(": ");
            data[i][0] = parts[0];
            data[i][1] = parts[1];
        }

        table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        leaderboardPanel.add(scrollPane);

        return leaderboardPanel;
    }
}

// TimerG Class
class TimerG implements TimerHandler {
    private static final int TIME_LIMIT = 20 * 60; // 20 minutes in seconds
    private javax.swing.Timer timer;
    private JLabel timerLabel;
    private int timeLeft = TIME_LIMIT;

    public TimerG() {
        timerLabel = new JLabel("Time left: 20:00", JLabel.CENTER);
        timerLabel.setBounds(10, 10, 340, 30);
        startTimer();
    }

    public JLabel getTimerLabel() {
        return timerLabel;
    }

    @Override
    public void startTimer() {
        timer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                int minutes = timeLeft / 60;
                int seconds = timeLeft % 60;
                timerLabel.setText(String.format("Time left: %02d:%02d", minutes, seconds));

                if (timeLeft <= 0) {
                    stopTimer();
                    // Handle quiz timeout
                }
            }
        });
        timer.start();
    }

    @Override
    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
}
