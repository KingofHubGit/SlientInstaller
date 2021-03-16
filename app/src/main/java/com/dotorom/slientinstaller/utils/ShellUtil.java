package com.dotorom.slientinstaller.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;


public class ShellUtil {

    private static final String TAG = "ShellUtil";

    private static final Object mLock = new Object();

    public static String execute(String command, long timeout) {
        return executeCmd1(command, timeout);
    }

    private static String executeCmd1(String command, final long timeout) {
        Log.d(TAG, "execute: " + command);
        synchronized (mLock) {
            String[] cmds = new String[]{"/system/bin/sh", "-c", command};
            try {
                ProcessBuilder builder = new ProcessBuilder(cmds);
                builder.redirectErrorStream(true);
                Process process = builder.start();
                WorkerThread workerThread = new WorkerThread(process);
                workerThread.start();
                try {
                    workerThread.join(timeout);
                } catch (InterruptedException e) {
                    workerThread.interrupt();
                }
                try {
                    if (null != process) {
                        int value = process.exitValue();
                        if (value == 0) {
                            process.destroy();
                        }
                    }
                } catch (IllegalThreadStateException e) {
                    process.destroy();
                }
                return workerThread.getResult();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }


    private static final class WorkerThread extends Thread {
        WeakReference<Process> mReference;
        private StringBuilder mStringBuilder = new StringBuilder();

        WorkerThread(Process process) {
            mReference = new WeakReference<>(process);
        }

        @Override
        public void run() {
            Process process = mReference.get();
            if (null == process) return;
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    mStringBuilder.append(line);
                    mStringBuilder.append("\n");
                    Log.d(TAG, "WorkerThread: " + line);
                }
                process.waitFor();
                process.exitValue();
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } finally {
            	try {
            		if(reader != null)
            			reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }

        String getResult() {
            return mStringBuilder.toString();
        }

    }

    public static String executeCmd2(String command, final long timeout) {
    	Log.d(TAG, "execute: " + command);
        synchronized (mLock) {
            final StringBuilder sb = new StringBuilder();
            String[] cmds = new String[]{"/system/bin/sh", "-c", command};
            try {
                Process process = Runtime.getRuntime().exec(cmds);
                final InputStream isNormal = process.getInputStream();
                final InputStream isError = process.getErrorStream();
                ReaderThread readerNormal = new ReaderThread("ReaderNormal", isNormal);
                ReaderThread readerError = new ReaderThread("ReaderError", isError);
                readerNormal.start();
                readerError.start();

                try {
                    readerNormal.join(timeout);
                } catch (Exception e) {
                    e.printStackTrace();
                    readerNormal.interrupt();
                }
                try {
                    readerError.join(timeout);
                } catch (Exception e) {
                    e.printStackTrace();
                    readerError.interrupt();
                }
                //process.waitFor();
                process.destroy();
                sb.append(readerNormal.getResult());
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(TAG, "execute: execute cmd end");
            return sb.toString();
        }
    }

    private static final class ReaderThread extends Thread {

        private InputStream is;
        private StringBuilder mStringBuilder;

        public String getResult() {
            return mStringBuilder.toString();
        }

        ReaderThread(String name, InputStream is) {
            super(name);
            this.is = is;
            mStringBuilder = new StringBuilder();
        }

        @Override
        public void run() {
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new InputStreamReader(is));
                String line;

                while (null != (line = reader.readLine())) {
                    mStringBuilder.append(line);
                    mStringBuilder.append("\n");
                }
                Log.d(TAG, Thread.currentThread().getName() + "," + mStringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            	try {
            		if(reader != null)
            			reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }

    }

    private static final class WaitingThread extends Thread {
        WeakReference<Process> mReference;
        private long timeout;

        WaitingThread(Process process, long timeout) {
            mReference = new WeakReference<>(process);
            this.timeout = timeout;
        }

        @Override
        public void run() {

            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (null != mReference.get()) {
                    mReference.get().destroy();
                }
                Log.d(TAG, Thread.currentThread().getName() + ",finally");
            }
        }
    }


}
