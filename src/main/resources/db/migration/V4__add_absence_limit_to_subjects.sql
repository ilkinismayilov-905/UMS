-- Migration V4: Add absence limit tracking to subjects table
ALTER TABLE subjects ADD COLUMN weekly_hours INT NOT NULL DEFAULT 30 AFTER credits;
ALTER TABLE subjects ADD COLUMN absence_limit INT NOT NULL DEFAULT 3 AFTER weekly_hours;

-- Create index for subject absence limit queries
CREATE INDEX idx_subject_absence_limit ON subjects(absence_limit);

