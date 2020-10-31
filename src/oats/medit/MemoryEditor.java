package oats.medit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MemoryEditor
{
    private final JFrame window;

    private final JLabel processName;

    private final JButton reScanButton;

    private final JTable addressTable;

    private final JComboBox<String> dataTypesList;
    private final JTextField scanValueField;

    private final JTextField newValueField;

    private ProcessInfo processInfo = new ProcessInfo("No process selected", 0);
    private List<Long> addressList;
    private int currentDataType;

    public MemoryEditor()
    {
        window = new JFrame("Medit");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800, 450);
        window.setLayout(null);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        JMenuItem selectProcessItem = new JMenuItem("Change process..");
        selectProcessItem.addActionListener(e -> changeProcess());

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(selectProcessItem);
        fileMenu.add(exitItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);

        processName = new JLabel("Process: No process selected");
        processName.setBounds(350, 0, 200, 20);

        addressTable = new JTable(new DefaultTableModel(new String[]{"Address", "Value"}, 0));
        addressTable.setBackground(new Color(0xC7C7C7));

        addressList = new ArrayList<>();

        JScrollPane scrollPane = new JScrollPane(addressTable);
        scrollPane.setBounds(40, 50, 300, 300);

        JLabel dataTypesLabel = new JLabel("Data Types");
        dataTypesLabel.setBounds(370, 30, 150, 20);

        dataTypesList = new JComboBox<>();
        dataTypesList.setBounds(370, 50, 150, 20);
        dataTypesList.addItem("8-bit int");
        dataTypesList.addItem("16-bit int");
        dataTypesList.addItem("32-bit int");
        dataTypesList.addItem("64-bit int");
        dataTypesList.addItem("32-bit float");
        dataTypesList.addItem("64-bit float");
        dataTypesList.setSelectedIndex(DataTypes.INT_64);

        JButton newScanButton = new JButton("New Scan");
        newScanButton.setBounds(530, 50, 100, 20);
        newScanButton.setFocusable(false);
        newScanButton.addActionListener(e -> newScanButtonCallback());

        reScanButton = new JButton("Re-Scan");
        reScanButton.setBounds(640, 50, 100, 20);
        reScanButton.setFocusable(false);
        reScanButton.addActionListener(e -> reScanButtonCallback());
        reScanButton.setEnabled(false);

        JLabel scanValueLabel = new JLabel("Scan value");
        scanValueLabel.setBounds(370, 80, 100, 20);

        scanValueField = new JTextField();
        scanValueField.setBounds(370, 100, 300, 40);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(370, 200, 100, 20);
        saveButton.setFocusable(false);
        saveButton.addActionListener(e -> saveButtonCallback());

        JLabel newValueLabel = new JLabel("New Value");
        newValueLabel.setBounds(370, 230, 100, 20);

        newValueField = new JTextField();
        newValueField.setBounds(370, 250, 300, 40);

        window.setJMenuBar(menuBar);

        window.add(processName);

        window.add(scrollPane);

        window.add(dataTypesLabel);
        window.add(dataTypesList);

        window.add(newScanButton);
        window.add(reScanButton);
        window.add(scanValueLabel);
        window.add(scanValueField);

        window.add(saveButton);
        window.add(newValueLabel);
        window.add(newValueField);

        window.setVisible(true);

        changeProcess();
    }

    private void reScanButtonCallback()
    {
        if(processInfo.handle == 0)
        {
            JOptionPane.showMessageDialog(window, "No process selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch(currentDataType)
        {
            case DataTypes.INT_8:
            {
                Byte scanValue = (Byte)getScanValue();
                if(scanValue == null)
                {
                    return;
                }

                ListIterator<Long> iter = addressList.listIterator();
                while(iter.hasNext())
                {
                    Long address = iter.next();

                    byte value = ProcessUtils.readByteAddress(processInfo.handle, address);
                    if(value != scanValue)
                    {
                        iter.remove();
                    }
                }
            } break;

            case DataTypes.INT_16:
            {
                Short scanValue = (Short)getScanValue();
                if(scanValue == null)
                {
                    return;
                }

                ListIterator<Long> iter = addressList.listIterator();
                while(iter.hasNext())
                {
                    Long address = iter.next();

                    short value = ProcessUtils.readShortAddress(processInfo.handle, address);
                    if(value != scanValue)
                    {
                        iter.remove();
                    }
                }
            } break;

            case DataTypes.INT_32:
            {
                Integer scanValue = (Integer)getScanValue();
                if(scanValue == null)
                {
                    return;
                }

                ListIterator<Long> iter = addressList.listIterator();
                while(iter.hasNext())
                {
                    Long address = iter.next();

                    int value = ProcessUtils.readIntAddress(processInfo.handle, address);
                    if(value != scanValue)
                    {
                        iter.remove();
                    }
                }
            } break;

            case DataTypes.INT_64:
            {
                Long scanValue = (Long)getScanValue();
                if(scanValue == null)
                {
                    return;
                }

                ListIterator<Long> iter = addressList.listIterator();
                while(iter.hasNext())
                {
                    Long address = iter.next();

                    long value = ProcessUtils.readLongAddress(processInfo.handle, address);
                    if(value != scanValue)
                    {
                        iter.remove();
                    }
                }
            } break;

            case DataTypes.FLOAT_32:
            {
                Float scanValue = (Float)getScanValue();
                if(scanValue == null)
                {
                    return;
                }

                ListIterator<Long> iter = addressList.listIterator();
                while(iter.hasNext())
                {
                    Long address = iter.next();

                    float value = ProcessUtils.readFloatAddress(processInfo.handle, address);
                    if(value != scanValue)
                    {
                        iter.remove();
                    }
                }
            } break;

            case DataTypes.FLOAT_64:
            {
                Double scanValue = (Double) getScanValue();
                if(scanValue == null)
                {
                    return;
                }

                ListIterator<Long> iter = addressList.listIterator();
                while(iter.hasNext())
                {
                    Long address = iter.next();

                    double value = ProcessUtils.readDoubleAddress(processInfo.handle, address);
                    if(value != scanValue)
                    {
                        iter.remove();
                    }
                }
            } break;

        }

        updateWindow();
    }

    private Object getValue(String value)
    {
        switch(currentDataType)
        {
            case DataTypes.INT_8:
            {
                byte result;

                try
                {
                    result = Byte.parseByte(value);
                }
                catch(NumberFormatException e)
                {
                    JOptionPane.showMessageDialog(window, "Entered value does not match data type.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                return result;
            }

            case DataTypes.INT_16:
            {
                short result;

                try
                {
                    result = Short.parseShort(value);
                }
                catch(NumberFormatException e)
                {
                    JOptionPane.showMessageDialog(window, "Entered value does not match data type.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                return result;
            }

            case DataTypes.INT_32:
            {
                int result;

                try
                {
                    result = Integer.parseInt(value);
                }
                catch(NumberFormatException e)
                {
                    JOptionPane.showMessageDialog(window, "Entered value does not match data type.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                return result;
            }

            case DataTypes.INT_64:
            {
                long result;

                try
                {
                    result = Long.parseLong(value);
                }
                catch(NumberFormatException e)
                {
                    JOptionPane.showMessageDialog(window, "Entered value does not match data type.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                return result;
            }

            case DataTypes.FLOAT_32:
            {
                float result;

                try
                {
                    result = Float.parseFloat(value);
                }
                catch(NumberFormatException e)
                {
                    JOptionPane.showMessageDialog(window, "Entered value does not match data type.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                return result;
            }

            case DataTypes.FLOAT_64:
            {
                double result;

                try
                {
                    result = Double.parseDouble(value);
                }
                catch(NumberFormatException e)
                {
                    JOptionPane.showMessageDialog(window, "Entered value does not match data type.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                return result;
            }
        }

        return null;
    }

    private Object getScanValue()
    {
        String value = scanValueField.getText();
        return getValue(value);
    }

    private Object getNewValue()
    {
        String value = newValueField.getText();
        return getValue(value);
    }

    private void newScanButtonCallback()
    {
        if(processInfo.handle == 0)
        {
            JOptionPane.showMessageDialog(window, "No process selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        reScanButton.setEnabled(true);
        currentDataType = dataTypesList.getSelectedIndex();
        Object value = getScanValue();
        if(value == null)
        {
            return;
        }

        resetAddressList();

        MemoryBlock memoryBlock = ProcessUtils.getNextMemoryBlock(processInfo.handle, null);
        while(memoryBlock != null)
        {
            if(memoryBlock.memory != null)
            {
                long memoryAddress = scanMemoryForValue(memoryBlock, currentDataType, value);
                while(memoryAddress > 0)
                {
                    addNewAddress(memoryAddress);
                    memoryAddress = scanMemoryForValue(memoryBlock, currentDataType, value);
                }
            }

            memoryBlock = ProcessUtils.getNextMemoryBlock(processInfo.handle, memoryBlock);
        }

        updateWindow();
    }

    private long scanMemoryForValue(MemoryBlock memoryBlock, int dataType, Object value)
    {
        ByteBuffer buffer = memoryBlock.memory;

        if(dataType == DataTypes.INT_8)
        {
            Byte sv = (Byte)value;

            while(buffer.hasRemaining())
            {
                byte v = buffer.get();
                if(v == sv)
                {
                    return buffer.position() + memoryBlock.baseAddress - 1;
                }
            }
        }
        else if(dataType == DataTypes.INT_16)
        {
            Short sv = (Short)value;

            while(buffer.hasRemaining())
            {
                short v = buffer.getShort();
                if(v == sv)
                {
                    return buffer.position() + memoryBlock.baseAddress - 2;
                }
            }
        }
        else if(dataType == DataTypes.INT_32)
        {
            Integer sv = (Integer)value;

            while(buffer.hasRemaining())
            {
                int v = buffer.getInt();
                if(v == sv)
                {
                    return buffer.position() + memoryBlock.baseAddress - 4;
                }
            }
        }
        else if(dataType == DataTypes.INT_64)
        {
            Long sv = (Long)value;

            while(buffer.hasRemaining())
            {
                long v = buffer.getLong();
                if(v == sv)
                {
                    return buffer.position() + memoryBlock.baseAddress - 8;
                }
            }
        }
        else if(dataType == DataTypes.FLOAT_32)
        {
            Float sv = (Float)value;

            while(buffer.hasRemaining())
            {
                float v = buffer.getFloat();
                if(v == sv)
                {
                    return buffer.position() + memoryBlock.baseAddress - 4;
                }
            }
        }
        else if(dataType == DataTypes.FLOAT_64)
        {
            Double sv = (Double)value;

            while(buffer.hasRemaining())
            {
                double v = buffer.getDouble();
                if(v == sv)
                {
                    return buffer.position() + memoryBlock.baseAddress - 8;
                }
            }
        }

        return 0;
    }

    private void saveButtonCallback()
    {
        Object value = getNewValue();
        int[] selected = addressTable.getSelectedRows();

        for(int i : selected)
        {
            Long address = addressList.get(i);
            writeData(address, value);
        }
    }

    private void writeData(long address, Object value)
    {
        switch(currentDataType)
        {
            case DataTypes.INT_8:
            {
                ProcessUtils.writeByteAddress(processInfo.handle, address, (Byte)value);
            } break;

            case DataTypes.INT_16:
            {
                ProcessUtils.writeShortAddress(processInfo.handle, address, (Short)value);
            } break;

            case DataTypes.INT_32:
            {
                ProcessUtils.writeIntAddress(processInfo.handle, address, (Integer)value);
            } break;

            case DataTypes.INT_64:
            {
                ProcessUtils.writeLongAddress(processInfo.handle, address, (Long)value);
            } break;

            case DataTypes.FLOAT_32:
            {
                ProcessUtils.writeFloatAddress(processInfo.handle, address, (Float)value);
            } break;

            case DataTypes.FLOAT_64:
            {
                ProcessUtils.writeDoubleAddress(processInfo.handle, address, (Double)value);
            } break;
        }
    }

    private void changeProcess()
    {
        ProcessChooser processChooser = new ProcessChooser();
        ProcessInfo temp = processChooser.getProcessInfo();

        if(temp.handle == 0)
        {
            return;
        }

        if(processInfo.handle != 0)
        {
            ProcessUtils.closeProcessHandle(processInfo.handle);
        }

        processInfo = temp;
        resetAddressList();
        updateWindow();
    }

    private void updateWindow()
    {
        processName.setText("Process: " + processInfo.name);

        DefaultTableModel model = (DefaultTableModel)addressTable.getModel();
        model.setRowCount(0);
        Object value = null;
        for(Long address : addressList)
        {
            if(value == null)
            {
                value = getScanValue();
            }

            model.addRow(new Object[]{Long.toHexString(address), value});
        }
    }

    private void addNewAddress(long address)
    {
        addressList.add(address);
    }

    private void resetAddressList()
    {
        addressList = new ArrayList<>();
    }
}
