package com.bigstride.jobportal_company.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bigstride.jobportal_company.Model.SkillsModel;
import com.bigstride.jobportal_company.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.ViewHolder> {

    private List<SkillsModel> skills;
    private List<String> selectedSkills;
    private ChipGroup chipGroup;

    public SkillsAdapter(List<SkillsModel> skills, List<String> selectedSkills, ChipGroup chipGroup) {
        this.skills = skills;
        this.selectedSkills = selectedSkills;
        this.chipGroup = chipGroup;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox skillCheckBox;
        public TextView skillNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            skillCheckBox = itemView.findViewById(R.id.skillCheckBox);
            skillNameTextView = itemView.findViewById(R.id.skillNameTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_skills, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SkillsModel skill = skills.get(position);
        holder.skillNameTextView.setText(skill.getName());

        holder.skillCheckBox.setOnCheckedChangeListener(null);
        holder.skillCheckBox.setChecked(selectedSkills.contains(skill.getName()));

        holder.skillCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedSkills.add(skill.getName());
                addSkillChip(skill.getName());
            } else {
                selectedSkills.remove(skill.getName());
                removeSkillChip(skill.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    private void addSkillChip(String skillName) {
        Chip chip = new Chip(chipGroup.getContext());
        chip.setText(skillName);
//        chip.setCloseIconVisible(true);
//        chip.setOnCloseIconClickListener(view -> {
//            selectedSkills.remove(skillName);
//            chipGroup.removeView(chip);
//        });
        chipGroup.addView(chip);
    }

    private void removeSkillChip(String skillName) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            View view = chipGroup.getChildAt(i);
            if (view instanceof Chip) {
                Chip chip = (Chip) view;
                if (chip.getText().toString().equals(skillName)) {
                    chipGroup.removeView(chip);
                    break;
                }
            }
        }
    }
}
