/*
 * TCSS 305 - Fall 2016
 * Assignment 4 - SnapShopGUI
 *
 */

package gui;

import filters.EdgeDetectFilter;
import filters.EdgeHighlightFilter;
import filters.Filter;
import filters.FlipHorizontalFilter;
import filters.FlipVerticalFilter;
import filters.GrayscaleFilter;
import filters.SharpenFilter;
import filters.SoftenFilter;

import image.PixelImage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;




/**
 * SnapShopGUI
 * This is the SnapShopGUI class.
 * 
 * @author Keldon Fischer
 * @version 10/31/2016
 *
 */
public final class SnapShopGUI extends JFrame {


    /**
     * The serial Version ID Code.
     */
    private static final long serialVersionUID = -8208233711915238864L;

    /**
     * initializes myFileChooser.
     */
    private final JFileChooser myFileChooser;

    /**
     * initializes myImageLabel.
     */
    private final JLabel myImageLabel;

    /**
     * initializes myCurrentImage.
     */
    private PixelImage myCurrentImage;

    /**
     * initializes myCenterPanel.
     */
    private final JPanel myCenterPanel;

    /**
     * initializes myWestPanel.
     */
    private JPanel myWestPanel;
    
    
    
    /**
     * This is the SnapShopGUI default constructor.
     */
    public SnapShopGUI() {
        super("TCSS 305 SnapShop");
        myCurrentImage = null;
        myImageLabel = new JLabel();
        myCenterPanel = new JPanel(new BorderLayout());
        myFileChooser = new JFileChooser(new File("."));
    }

    /**
     * This is the Start method which produces my GUI.
     */
    public void start() {
        javax.swing.JOptionPane.showMessageDialog(null, "SnapShop placeholder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        myWestPanel = new JPanel(new BorderLayout());
        myWestPanel.add(filterButtonPanel(), BorderLayout.NORTH);
        myWestPanel.add(imageButtonPanel(), BorderLayout.SOUTH);
      
        final JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(myCenterPanel, BorderLayout.NORTH);
      
        add(myWestPanel, BorderLayout.WEST);
        add(myCenterPanel, BorderLayout.NORTH);

        pack();
        this.setMinimumSize(getMinimumSize());
        setVisible(true);
    }
    
    /**
     * This method adds all of my filter buttons to my filter panel.
     * @return filterButtonPanel
     */
    private JPanel filterButtonPanel() {
        final JButton edgeDetectFilterButton = makeFilter(new EdgeDetectFilter());
        final JButton edgeHighlightFilterButton = makeFilter(new EdgeHighlightFilter());
        final JButton flipHorizontalFilterButton = makeFilter(new FlipHorizontalFilter());
        final JButton flipVerticalFilterButton = makeFilter(new FlipVerticalFilter());
        final JButton grayscaleFilterButton = makeFilter(new GrayscaleFilter());
        final JButton sharpenFilterButton = makeFilter(new SharpenFilter());
        final JButton softenFilterButton = makeFilter(new SoftenFilter());

        final JPanel filterButtonPanel = new JPanel(new GridLayout(7, 1));
        filterButtonPanel.add(edgeDetectFilterButton);
        filterButtonPanel.add(edgeHighlightFilterButton);
        filterButtonPanel.add(flipHorizontalFilterButton);
        filterButtonPanel.add(flipVerticalFilterButton);
        filterButtonPanel.add(grayscaleFilterButton);
        filterButtonPanel.add(sharpenFilterButton);
        filterButtonPanel.add(softenFilterButton);
        
        return filterButtonPanel;
    }
    
    /**
     * This method makes a button that grabs a filter and transforms my 
     * image depending on the filter.
     * @param theFilter is where i put my filters.
     * @return filterButton
     */
    private JButton makeFilter(final Filter theFilter) {
        final JButton filterButton = new JButton(theFilter.getDescription());
        filterButton.addActionListener((theEvent) -> {

            try {
                theFilter.filter(myCurrentImage);
                myImageLabel.setIcon(new ImageIcon(myCurrentImage));

                myCenterPanel.removeAll();
                myCenterPanel.add(myImageLabel);

                add(myCenterPanel, BorderLayout.CENTER);

            } catch (final NullPointerException exception) {
                JOptionPane.showMessageDialog(null, "No Image to use filter on.");
            }

        });

        if (null == myCurrentImage) {
            filterButton.setEnabled(false);
        }

        return filterButton;
    }

    /**
     * This method makes and adds buttons to my panel; "open", "save as", and "close image".
     * @return imageButtonPanel
     */
    private JPanel imageButtonPanel() {

        final JButton openButton = new JButton("Open...");
        openButton.addActionListener((theEvent) -> {
            if (myFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                final File imageFile = new File(myFileChooser.getSelectedFile().getPath());

                try {
                    myCurrentImage = PixelImage.load(imageFile);

                    myImageLabel.setIcon(new ImageIcon(myCurrentImage));

                    myCenterPanel.removeAll();
                    myCenterPanel.add(myImageLabel, BorderLayout.NORTH);

                    add(myCenterPanel, BorderLayout.CENTER);
                    
                    if (null != myCurrentImage) {
                        myWestPanel.removeAll();
                        myWestPanel.add(filterButtonPanel(), BorderLayout.NORTH);
                        myWestPanel.add(imageButtonPanel(), BorderLayout.SOUTH);
                    }
                    
                    
                    pack();
                    setMinimumSize(getSize()); //Set new minimum size after image is loaded.
                } catch (final IOException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        });

        final JButton saveAsButton = new JButton("Save As...");
        saveAsButton.addActionListener((theEvent) -> {
            if (myFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    myCurrentImage.save(myFileChooser.getSelectedFile());
                } catch (final IOException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
            
        });
        if (null == myCurrentImage) {
            saveAsButton.setEnabled(false);
        }
        
        

        final JButton closeImageButton = new JButton("Close Image");
        closeImageButton.addActionListener((theEvent) -> {
            remove(myCenterPanel);
            this.setMinimumSize(new Dimension(0, 0)); //resets image size to zero.
            myCurrentImage = null;
            
            if (null == myCurrentImage) {
                myWestPanel.removeAll();
                myWestPanel.add(filterButtonPanel(), BorderLayout.NORTH);
                myWestPanel.add(imageButtonPanel(), BorderLayout.SOUTH);
            }
            
            pack();
            setMinimumSize(getSize()); //Set new minimum size after image is deleted.
        });
        if (null == myCurrentImage) {
            closeImageButton.setEnabled(false);
        }

        final JPanel imageButtonPanel = new JPanel(new GridLayout(3, 1));
        imageButtonPanel.add(openButton);
        imageButtonPanel.add(saveAsButton);
        imageButtonPanel.add(closeImageButton);
        
        

        return imageButtonPanel;

    } //---------  End of ImageButtonPanel Method  -----------------------------------------
    


} // end SnapShop class