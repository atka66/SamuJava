/*
 * @brief SAMU - the potential ancestor of developmental robotics chatter bots
 *
 * @file samu.hpp
 * @author  Norbert Bátfai <nbatfai@gmail.com>
 * @version 0.0.1
 *
 * @section LICENSE
 *
 * Copyright (C) 2015 Norbert Bátfai, batfai.norbert@inf.unideb.hu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @section DESCRIPTION
 * SAMU
 * 
 * The main purpose of this project is to allow the evaluation and 
 * verification of the results of the paper entitled "A disembodied 
 * developmental robotic agent called Samu Bátfai". It is our hope 
 * that Samu will be the ancestor of developmental robotics chatter 
 * bots that will be able to chat in natural language like humans do.
 *
 */
package samujava;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Samu {

    public Samu() {
        mutex_ = new ReentrantLock();
        vi = new VisualImagery();
        nlp = new NLP();
        caregiver_name_ = new ArrayList<>();
        caregiver_name_.add("Norbi");
        caregiver_name_.add("Nandi");
        caregiver_name_.add("Matyi");
        caregiver_name_.add("Greta");
    }

    public Boolean run() {
        return run_;
    }

    public void FamilyCaregiverShell() {

        String cmd_prefix = "___";

        System.out.print(Caregiver() + "@Caregiver> ");

        BufferedReader stdin_reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            for (String line; (line = stdin_reader.readLine()) != null;) {
                if (line.substring(0, cmd_prefix.length()).equals(cmd_prefix)) {
                    if (line == cmd_prefix) {
                        NextCaregiver();
                    }
                } else {
                    putIn(line);
                }

                System.out.print(Caregiver() + "@Caregiver> ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        run_ = false;

    }

    public void terminal() {
        mutex_.lock();
//     cv_.wait ( lk );

        FamilyCaregiverShell();
        mutex_.unlock();
    }

    public void putIn(String sentence) //   void operator<< ( std::string sentence )
    {
        vi.putIn(nlp.sentence2triplets(sentence));
    }

    public String Caregiver() {
        if (caregiver_name_.size() > 0) {
            return caregiver_name_.get(caregiver_idx_);
        } else {
            return "Undefined";
        }
    }

    public void NextCaregiver() {
        caregiver_idx_ = (caregiver_idx_ + 1) % caregiver_name_.size();
    }

    public double reward() {
        return vi.reward();
    }

    private Boolean run_ = true;
    private Lock mutex_;

    private NLP nlp;
    private VisualImagery vi;

    private int caregiver_idx_ = 0;
    private List<String> caregiver_name_;
};
