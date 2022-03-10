package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDao implements TransferDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Transfer> getHistory(long id) {
        String sql = "select transfer_id, transfer_type.transfer_type_desc, transfer_status.transfer_status_desc, uf.username as user_from, uto.username as user_to, amount\n" +
                "from transfer \n" +
                "join transfer_status \n" +
                "on transfer_status.transfer_status_id = transfer.transfer_status_id \n" +
                "join transfer_type \n" +
                "on transfer_type.transfer_type_id = transfer.transfer_type_id\n" +
                "join account af on af.account_id = transfer.account_from \n" +
                "join account ato on ato.account_id = transfer.account_to\n" +
                "join tenmo_user uf on uf.user_id = af.user_id \n" +
                "join tenmo_user uto on uto.user_id = ato.user_id " +
                "where uf.user_id = ? or uto.user_id = ?";
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(sql, id, id);

        List<Transfer> results = new ArrayList<>();
        while(resultSet.next()){
            results.add(mapRowToTransfer(resultSet));
        }
        return results;
    }

    @Override
    public List<Transfer> viewPendingRequests(long id) {
        String sql = "select transfer_id, transfer_type.transfer_type_desc, transfer_status.transfer_status_desc, uf.username as user_from, uto.username as user_to, amount\n" +
                "from transfer \n" +
                "join transfer_status \n" +
                "on transfer_status.transfer_status_id = transfer.transfer_status_id \n" +
                "join transfer_type \n" +
                "on transfer_type.transfer_type_id = transfer.transfer_type_id\n" +
                "join account af on af.account_id = transfer.account_from \n" +
                "join account ato on ato.account_id = transfer.account_to\n" +
                "join tenmo_user uf on uf.user_id = af.user_id \n" +
                "join tenmo_user uto on uto.user_id = ato.user_id " +
                "where uf.user_id = ? AND transfer_status_desc='Pending'";
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(sql, id);

        List<Transfer> results = new ArrayList<>();
        while(resultSet.next()){
            results.add(mapRowToTransfer(resultSet));
        }
        return results;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer VALUES (default,(select transfer_type_id from transfer_type where transfer_type_desc = ?),(select transfer_type_status from transfer_status where transfer_status_desc = ?),?,?,?) RETURNING transfer_id;";
        long newId = jdbcTemplate.queryForObject(sql,long.class, transfer.getTransferType(),transfer.getTransferStatus(),
                transfer.getUserFrom(),transfer.getUserTo(),transfer.getAmount());
        transfer.setId(newId);
        return transfer;
    }

    @Override
    public void updatePending(String status, Long id) {
        String sql = "UPDATE transfer SET transfer_status_id = \n" +
                "\t(SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = ? )\n" +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sql,status,id);
    }

    @Override
    public Transfer getTransferByID(Long id) {
        String sql = "select transfer_id, transfer_type.transfer_type_desc, transfer_status.transfer_status_desc, uf.username as user_from, uto.username as user_to, amount\n" +
                "from transfer \n" +
                "join transfer_status \n" +
                "on transfer_status.transfer_status_id = transfer.transfer_status_id \n" +
                "join transfer_type \n" +
                "on transfer_type.transfer_type_id = transfer.transfer_type_id\n" +
                "join account af on af.account_id = transfer.account_from \n" +
                "join account ato on ato.account_id = transfer.account_to\n" +
                "join tenmo_user uf on uf.user_id = af.user_id \n" +
                "join tenmo_user uto on uto.user_id = ato.user_id " +
                "where transfer_id = ?";
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(sql, id);
        if(resultSet.next()){
            return mapRowToTransfer(resultSet);
        }
        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet resultSet){
        Transfer transfer = new Transfer();
        transfer.setId(resultSet.getLong("transfer_id"));
        transfer.setTransferType(resultSet.getString("transfer_type_desc"));
        transfer.setTransferStatus(resultSet.getString("transfer_status_desc"));
        transfer.setUserFrom(resultSet.getString("user_from"));
        transfer.setUserTo(resultSet.getString("user_to"));
        transfer.setAmount(resultSet.getBigDecimal("amount"));
        return transfer;
    }

}
