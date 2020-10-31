package oats.medit;

import javax.swing.*;

public class ProcessChooser
{
    private JDialog window;

    private JComboBox<String> processList;

    private ProcessInfo info = new ProcessInfo(null, 0);
    private boolean processSelected = false;
    private int[] pids;

    public ProcessInfo getProcessInfo()
    {
        window = new JDialog();
        window.setModal(true);
        window.setTitle("Medit");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(500, 250);
        window.setLayout(null);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        JLabel label = new JLabel("Choose a process:");
        label.setBounds(50, 10, 200, 30);

        JButton scanButton = new JButton("scan");
        scanButton.setBounds(50, 50, 100, 50);
        scanButton.setFocusable(false);
        scanButton.addActionListener(e -> scanProcesses());

        JButton selectButton = new JButton("select");
        selectButton.setBounds(50, 125, 100, 50);
        selectButton.setFocusable(false);
        selectButton.addActionListener(e -> {
            getSelectedProcess();
            window.dispose();
        });

        processList = new JComboBox<>();
        processList.setBounds(200, 50, 250, 25);

        scanProcesses();

        window.add(label);
        window.add(scanButton);
        window.add(selectButton);
        window.add(processList);
        window.setVisible(true);

        return info;
    }

    private int findProcessPidByName(String name)
    {
        for(int pid : pids)
        {
            String pName = ProcessUtils.getProcessName(pid);
            if(pName != null)
            {
                if(pName.equals(name))
                {
                    return pid;
                }
            }
        }

        return 0;
    }

    private void getSelectedProcess()
    {
        String name = (String)processList.getSelectedItem();
        int pid = findProcessPidByName(name);
        long handle = ProcessUtils.getProcessHandle(pid);

        info = new ProcessInfo(name, handle);
        processSelected = true;
    }

    private void scanProcesses()
    {
        processList.removeAllItems();

        pids = ProcessUtils.getActiveProcessList();
        if(pids == null)
        {
            JOptionPane.showMessageDialog(window, "Process scan failed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for(int pid : pids)
        {
            String name = ProcessUtils.getProcessName(pid);
            if(name != null)
            {
                processList.addItem(name);
            }
        }


    }
}
