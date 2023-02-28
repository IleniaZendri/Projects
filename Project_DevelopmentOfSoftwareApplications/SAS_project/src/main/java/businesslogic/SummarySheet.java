/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sandrone e Zendri
 */

public class SummarySheet {
    private List<Job> jobs;
    private User owner;
    
    public SummarySheet(User owner) {
        this.owner = owner;
        this.jobs = new ArrayList<>();
    }
    
    public void addJob(Job job) {
        jobs.add(job);
    }
    
    public List<Job> getJobs() {
        return this.jobs;
    }
    
    public boolean removeJob(Job j) {
        return jobs.remove(j);
    }
    
    //Rimuove tutti i compiti legati ad una preparazione
    public void removeAllJobs(Recipe r)
    {
        jobs.removeIf(j -> j.getRecipe() == r);
    }
    
    public boolean hasJob(Job job) {
        return jobs.contains(job);
    }

    public void moveJob(Job job, int pos) {
        jobs.remove(job);
        jobs.add(pos, job);
    }
            
   public int getJobPosition(Job job) {
       return jobs.indexOf(job);
   }
}
