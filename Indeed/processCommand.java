/*
  上机coding， 实现几个函数，有user找job，然后每个job有多个steps， 每个函数有要求的输出格式或着没有输出
  1. add step - add <user> <job> <step> 添加step
  2. complete step - complete <user> <job> 把下一步完成，但是后边的不完成
  3. query <job> 给一个job，输出所有没有完成的user和相应的current step，一行一行输出
  4. expire - expire <job>把这个所有步骤删除
  有些函数还有一些‍‌‍‌‍‍‌‌‌‍‌‌‍‌‌‍‌‍‍corner case要return no result。输入是一些上面的函数，输出是要求函数相应的输出
*/

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    static Map<Long, Map<Long, Queue<String>>> userMap;             //user id to job id  job id has string1,string2,string3...
    // static Map<Long, Queue<String>> job;                        //jobid with actions
    
    public static void processCommand(String command) {

        // Use System.out.println() to print result when needed
        // userid   job id
        String[] strs = command.split(" ");
        if (strs.length <= 1) {
            return;
        }
        // ADD [type] [user-ID] [job-ID]
        if (strs[0].equals("ADD")) {
            String type = strs[1];
            long userId = Long.parseLong(strs[2]);
            long jobId = Long.parseLong(strs[3]);
            // from user id -- job id
            userMap.putIfAbsent(userId, new HashMap<>());
            // from job id to type sequence 
            Map<Long, Queue<String>> jobMap = userMap.get(userId);
            jobMap.putIfAbsent(jobId, new LinkedList<>());
            jobMap.get(jobId).add(type);
            
        } 
        // QUERY [user-ID]
        else if (strs[0].equals("QUERY")) {
            long userId = Long.parseLong(strs[1]);
            //for loop here
            Map<Long, Queue<String>> jobMap = userMap.get(userId);
            List<Pair> ans = new ArrayList<>();
            for (long jobId : jobMap.keySet()) {
                //id + sequece    
                ans.add(new Pair(jobId, jobMap.get(jobId).peek()));
            }
            Collections.sort(ans, (p1, p2) -> (Long.compare(p1.jobId, p2.jobId)));       //sort based on job id
            
            if (ans.size() == 0) {
                // Next steps for user
                System.out.println("No result");    
            } else {
                // Next steps for user 1:
                System.out.println("Next steps for user " + userId + ":");
                for (Pair p : ans) {
                    System.out.println("    " + p.jobId + " " + p.action);
                }
            } 
        } 
        // COMPLETE [user-ID] [job-ID]
        else if (strs[0].equals("COMPLETE")){
            long userId = Long.parseLong(strs[1]);
            long jobId = Long.parseLong(strs[2]);
            if (userMap.get(userId) == null) {
                return;     //cannot find user id
            }
            Map<Long, Queue<String>> jobMap = userMap.get(userId);
            if (jobMap.get(jobId) == null) {
                return;
            }
            //find the next one to completed
            String nextAction = jobMap.get(jobId).poll();
            if (jobMap.get(jobId).size() == 0) {
                jobMap.remove(jobId);
            }
            //User 1 completed apply for job 10
            System.out.println("User " + userId + " completed " + nextAction + " for job " + jobId);
        } 
        // EXPIRE [job-ID]
        else if (strs[0].equals("EXPIRE")) {
            long jobId = Long.parseLong(strs[1]);
            for (long userId : userMap.keySet()) {
                Map<Long, Queue<String>> jobMap = userMap.get(userId);
                if (jobMap.containsKey(jobId)) {
                    //remove en
                    jobMap.remove(jobId);
                }
            }
            //other case cannot find such key
        } else {
            //
            return;
        }
    }
    
    static class Pair {
        long jobId;
        String action;
        Pair(long jobId, String action) {
            this.jobId = jobId;
            this.action = action;
        }
    }
    
    public static void main(String args[] ) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final int N = Integer.parseInt(br.readLine());
        // Read and process commands
        
        userMap = new HashMap<>();
        
        for (int i = 0; i < N; i++) {
            processCommand(br.readLine());
        }
    }
}
