package com.voxelplugineering.voxelsniper.common.commands;

import java.util.Map;

import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.command.Command;
import com.voxelplugineering.voxelsniper.common.command.CommandArgument;
import com.voxelplugineering.voxelsniper.common.command.args.RawArgument;

public class BrushCommand extends Command
{

    public BrushCommand()
    {
        super("brush", "Sets your current brush");
        setAliases("b", "brush");
        addArgument(new RawArgument("raw"));
    }

    @Override
    public boolean execute(ISniper sniper, Map<String, CommandArgument> args)
    {
        String[] s = ((RawArgument) args.get("raw")).getValue();
        if (s.length >= 1)
        {
            String brush = s[0];

        }
        return false;
    }

}
