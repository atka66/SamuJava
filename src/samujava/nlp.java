/*
 * @brief SAMU - the potential ancestor of developmental robotics chatter bots
 *
 * @file nlp.hpp
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

import java.util.*;
import org.linkgrammar.LinkGrammar;
import org.linkgrammar.Linkage;

class NLP {

    NLP() {
        LinkGrammar.init();
    }

    public List<SPOTriplet> sentence2triplets(String sentence) {
        List<SPOTriplet> triplets = new ArrayList<SPOTriplet>();
        LinkGrammar.parse(sentence);

        int num_linkages = LinkGrammar.getNumLinkages();
        SPOTriplet triplet = new SPOTriplet();
        String alter_p = new String();

        Boolean ready = false;

        for (int l = 0; l < num_linkages; ++l) {

            LinkGrammar.makeLinkage(l);
            Linkage linkage = new Linkage();

            linkage.setDisjunctCost(LinkGrammar.getLinkageDisjunctCost());
            linkage.setLinkCost(LinkGrammar.getLinkageLinkCost());
            linkage.setLinkedWordCount(LinkGrammar.getNumWords());
            linkage.setNumViolations(LinkGrammar.getLinkageNumViolations());

            String[] disjuncts = new String[LinkGrammar.getNumWords()];
            String[] words = new String[LinkGrammar.getNumWords()];

            for (int k = 0; k < words.length; k++) {

                disjuncts[k] = LinkGrammar.getLinkageDisjunct(k);
                words[k] = LinkGrammar.getLinkageWord(k);
            }

            int linkage_get_num_link = LinkGrammar.getNumLinks();

            for (int k = 0; k < linkage_get_num_link && !ready; k++) {
                String c = LinkGrammar.getLinkLabel(k);

                if (c.charAt(0) == 'S') {
                    triplet.p = LinkGrammar.getLinkageWord(k);

                    alter_p = words[LinkGrammar.getLinkRWord(k)];

                    triplet.s = words[LinkGrammar.getLinkLWord(k)];
                }

                if (c.charAt(0) == 'O') {
                    triplet.o = words[LinkGrammar.getLinkRWord(k)];

                    if (triplet.p.equals(words[LinkGrammar.getLinkRWord(k)])) {

                        triplet.cut();

                        triplets.add(triplet);

                        ready = true;
                        break;
                    } else if (alter_p.equals(words[LinkGrammar.getLinkRWord(k)])) {
                        triplet.p = alter_p;

                        triplet.cut();

                        triplets.add(triplet);

                        ready = true;
                        break;

                    }
                }
            }
        }

        return triplets;
    }

};
